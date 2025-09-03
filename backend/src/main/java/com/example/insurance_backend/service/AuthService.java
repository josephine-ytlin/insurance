package com.example.insurance_backend.service;

import com.example.insurance_backend.dto.ApiResponse;
import com.example.insurance_backend.dto.LoginRequest;
import com.example.insurance_backend.model.User;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public ApiResponse<String> loginOrRegister(LoginRequest req, HttpSession session, HttpServletRequest request) {
        String username = req.getUsername();
        String password = req.getPassword();
        String origin = request.getHeader("Origin");

        logger.info("登入請求，使用者帳號: {}", username);

        User user = userService.findByUsername(username);

        // 新用戶註冊
        if (user == null) {
            if (!isValidEmail(username)) {
                logger.warn("登入失敗，無效 email: {}", username);
                return new ApiResponse(false, "請輸入有效的 email", username);
            }
            logger.info("新用戶註冊: {}", username);
            userService.register(username, password, origin);
        }

        // 登入驗證
        User loginUser = userService.login(username, password);
        if (loginUser == null) {
            logger.warn("登入失敗，帳號或密碼錯誤: {}", username);
            return new ApiResponse(false, "帳號或密碼錯誤", username);
        }

        if (!loginUser.isVerified()) {
            logger.warn("登入失敗，未完成 Email 驗證: {}", username);
            return new ApiResponse(false, "請先完成 Email 驗證", username);
        }

        // 設定 SecurityContext 與 Session
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(loginUser, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        session.setAttribute("user", loginUser);

        logger.info("使用者登入成功: {}", username);
        return new ApiResponse(true, "登入成功", username);
    }

    public ApiResponse<Void> verifyEmail(String token) {
        logger.info("驗證請求，token: {}", token);
        User user = userService.findByToken(token);
        if (user == null) {
            logger.warn("驗證失敗，無效或已使用 token: {}", token);
            throw new IllegalArgumentException("驗證連結無效或已使用");
        }
        user.setVerified(true);
        user.setVerificationToken(null);
        userService.save(user);
        logger.info("驗證成功，使用者: {}", user.getUsername());
        return new ApiResponse(true, "驗證成功，請重新登入", null);
    }

    public ApiResponse<Void> logout(HttpSession session) {
        logger.info("登出請求");
        session.invalidate();
        logger.info("登出成功");
        return new ApiResponse(true, "已登出", null);
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}

