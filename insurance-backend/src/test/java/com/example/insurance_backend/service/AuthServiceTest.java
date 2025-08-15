package com.example.insurance_backend.service;

import com.example.insurance_backend.dto.ApiResponse;
import com.example.insurance_backend.dto.LoginRequest;
import com.example.insurance_backend.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

//	新用戶註冊成功並登入成功
//
//	已有用戶登入成功
//
//	帳密錯誤
//
//	未驗證的帳號登入
//
//	註冊時 Email 格式錯誤
//
//	verifyEmail 成功
//
//	verifyEmail 失敗
//
//	logout 成功
    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void loginOrRegister_newUser_success() {
        LoginRequest req = new LoginRequest();
        req.setUsername("newuser@example.com");
        req.setPassword("password");

        when(request.getHeader("Origin")).thenReturn("http://localhost");
        when(userService.findByUsername("newuser@example.com")).thenReturn(null);

        User loginUser = new User();
        loginUser.setUsername("newuser@example.com");
        loginUser.setVerified(true);
        when(userService.login("newuser@example.com", "password")).thenReturn(loginUser);

        ApiResponse<String> response = authService.loginOrRegister(req, session, request);

        assertTrue(response.isSuccess());
        assertEquals("登入成功", response.getMessage());
        verify(userService).register("newuser@example.com", "password", "http://localhost");
        verify(session).setAttribute(eq("user"), any(User.class));
    }

    @Test
    void loginOrRegister_existingUser_success() {
        LoginRequest req = new LoginRequest();
        req.setUsername("exist@example.com");
        req.setPassword("password");

        User existingUser = new User();
        existingUser.setUsername("exist@example.com");
        when(userService.findByUsername("exist@example.com")).thenReturn(existingUser);

        existingUser.setVerified(true);
        when(userService.login("exist@example.com", "password")).thenReturn(existingUser);

        ApiResponse<String> response = authService.loginOrRegister(req, session, request);

        assertTrue(response.isSuccess());
        verify(session).setAttribute(eq("user"), any(User.class));
    }

    @Test
    void loginOrRegister_wrongPassword() {
        LoginRequest req = new LoginRequest();
        req.setUsername("exist@example.com");
        req.setPassword("wrong");

        when(userService.findByUsername("exist@example.com")).thenReturn(new User());
        when(userService.login("exist@example.com", "wrong")).thenReturn(null);

        ApiResponse<String> response = authService.loginOrRegister(req, session, request);

        assertFalse(response.isSuccess());
        assertEquals("帳號或密碼錯誤", response.getMessage());
    }

    @Test
    void loginOrRegister_notVerified() {
        LoginRequest req = new LoginRequest();
        req.setUsername("exist@example.com");
        req.setPassword("password");

        User user = new User();
        user.setUsername("exist@example.com");
        user.setVerified(false);

        when(userService.findByUsername("exist@example.com")).thenReturn(user);
        when(userService.login("exist@example.com", "password")).thenReturn(user);

        ApiResponse<String> response = authService.loginOrRegister(req, session, request);

        assertFalse(response.isSuccess());
        assertEquals("請先完成 Email 驗證", response.getMessage());
    }

    @Test
    void loginOrRegister_invalidEmail() {
        LoginRequest req = new LoginRequest();
        req.setUsername("invalidEmail");
        req.setPassword("password");

        when(userService.findByUsername("invalidEmail")).thenReturn(null);

        ApiResponse<String> response = authService.loginOrRegister(req, session, request);

        assertFalse(response.isSuccess());
        assertEquals("請輸入有效的 email", response.getMessage());
        verify(userService, never()).register(any(), any(), any());
    }

    @Test
    void verifyEmail_success() {
        User user = new User();
        user.setUsername("test@example.com");

        when(userService.findByToken("token123")).thenReturn(user);

        ApiResponse<Void> response = authService.verifyEmail("token123");

        assertTrue(response.isSuccess());
        assertEquals("驗證成功，請重新登入", response.getMessage());
        assertTrue(user.isVerified());
        assertNull(user.getVerificationToken());
        verify(userService).save(user);
    }

    @Test
    void verifyEmail_tokenNotFound() {
        when(userService.findByToken("badToken")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> authService.verifyEmail("badToken"));
    }

    @Test
    void logout_success() {
        ApiResponse<Void> response = authService.logout(session);

        assertTrue(response.isSuccess());
        assertEquals("已登出", response.getMessage());
        verify(session).invalidate();
    }
}
