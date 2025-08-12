package com.example.insurance_backend.controller;

import com.example.insurance_backend.entity.User;
import com.example.insurance_backend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login")
    @Operation(summary = "登入或註冊")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req, HttpSession session,HttpServletRequest request) {
    	String origin = request.getHeader("Origin");   // 來源域名
//        String referer = request.getHeader("Referer"); // 來源頁面 URL
        
    	
    	String username = req.get("username");
        String password = req.get("password");

        logger.info("登入請求，使用者帳號: {}", username);

        try {
            User user = userService.findByUsername(username);
            boolean isNewUser = false;

            if (user == null) {
                if (!username.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    logger.warn("登入失敗，無效的 email: {}", username);
                    return ResponseEntity.badRequest().body(Map.of("message", "請輸入有效的 email"));
                }
                userService.register(username, password, origin);
                user = userService.findByUsername(username);
            }

            User loginUser = userService.login(username, password);
            if (loginUser != null) {

                if (!loginUser.isVerified()) {
                    isNewUser = true;
                    logger.warn("使用者尚未驗證，拒絕登入: {}", username);
                    Map<String, Object> resp = new HashMap<>();
                    resp.put("message", "請先完成 Email 驗證");
                    resp.put("isNewUser", isNewUser);
                    return ResponseEntity.status(403).body(resp);
                }

                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(loginUser, null, List.of());

                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(authentication);
                session.setAttribute("SPRING_SECURITY_CONTEXT", context);
                session.setAttribute("user", loginUser);

                logger.info("使用者登入成功: {}", username);

                Map<String, Object> resp = new HashMap<>();
                resp.put("message", "success");
                resp.put("username", loginUser.getUsername());
                resp.put("isNewUser", isNewUser);
                return ResponseEntity.ok(resp);
            } else {
                logger.warn("登入失敗，帳號或密碼錯誤: {}", username);
                return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
            }
        } catch (Exception e) {
            logger.error("登入過程發生錯誤，使用者: {}", username, e);
            return ResponseEntity.status(500).body(Map.of("message", "登入失敗，請稍後再試"));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        logger.info("驗證請求，token: {}", token);
        try {
            User user = userService.findByToken(token);
            if (user != null) {
                user.setVerified(true);
                user.setVerificationToken(null);
                userService.save(user);
                logger.info("驗證成功，使用者: {}", user.getUsername());
                return ResponseEntity.ok(Map.of("message", "驗證成功，請重新登入"));
            } else {
                logger.warn("驗證失敗，無效或已使用的token: {}", token);
                return ResponseEntity.status(400).body(Map.of("message", "驗證連結無效或已使用"));
            }
        } catch (Exception e) {
            logger.error("驗證過程發生錯誤，token: {}", token, e);
            return ResponseEntity.status(500).body(Map.of("message", "驗證失敗，請稍後再試"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        logger.info("登出請求");
        try {
            session.invalidate();
            logger.info("登出成功");
            return ResponseEntity.ok(Map.of("message", "logged out"));
        } catch (Exception e) {
            logger.error("登出過程發生錯誤", e);
            return ResponseEntity.status(500).body(Map.of("message", "登出失敗"));
        }
    }
}
