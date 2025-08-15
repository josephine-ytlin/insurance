package com.example.insurance_backend.service;

import com.example.insurance_backend.entity.User;
import com.example.insurance_backend.mapper.UserMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserMapper userMapper;

	@Mock
	private EmailService emailService;

	private final PasswordEncoder encoder = new BCryptPasswordEncoder();

	private UserService userService;

	@BeforeEach
	void setUp() {
	    MockitoAnnotations.openMocks(this);
	    userService = new UserService(userMapper, emailService, encoder);
	}
    // 1. register() 測試
    @Test
    void register_shouldEncodePasswordAndSendEmail() {
        String username = "test@example.com";
        String password = "123456";
        String origin = "http://localhost";

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        userService.register(username, password, origin);

        // 驗證 mapper 被呼叫
        verify(userMapper, times(1)).insertUser(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals(username, savedUser.getUsername());
        assertTrue(encoder.matches(password, savedUser.getPasswordHash()));
        assertNotNull(savedUser.getVerificationToken());
        assertFalse(savedUser.isVerified());

        // 驗證 emailService 被呼叫
        verify(emailService, times(1))
                .sendVerificationEmail(eq(username), eq(savedUser.getVerificationToken()), eq(origin));
    }

    // 2. login 成功
    @Test
    void login_success() {
        String username = "user@example.com";
        String rawPassword = "password";
        String hashedPassword = encoder.encode(rawPassword);

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPasswordHash(hashedPassword);

        when(userMapper.findByUsername(username)).thenReturn(mockUser);

        User result = userService.login(username, rawPassword);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    // 3. login 找不到使用者
    @Test
    void login_userNotFound() {
        when(userMapper.findByUsername("noUser")).thenReturn(null);

        User result = userService.login("noUser", "password");

        assertNull(result);
    }

    // 4. login 密碼錯誤
    @Test
    void login_wrongPassword() {
        String username = "user@example.com";
        String hashedPassword = encoder.encode("correct");

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPasswordHash(hashedPassword);

        when(userMapper.findByUsername(username)).thenReturn(mockUser);

        User result = userService.login(username, "wrong");

        assertNull(result);
    }

    // 5. findByUsername
    @Test
    void findByUsername_shouldCallMapper() {
        String username = "test";
        User user = new User();
        user.setUsername(username);

        when(userMapper.findByUsername(username)).thenReturn(user);

        User result = userService.findByUsername(username);

        assertEquals(user, result);
        verify(userMapper, times(1)).findByUsername(username);
    }

    // 6. findByToken
    @Test
    void findByToken_shouldCallMapper() {
        String token = "token123";
        User user = new User();

        when(userMapper.findByToken(token)).thenReturn(user);

        User result = userService.findByToken(token);

        assertEquals(user, result);
        verify(userMapper, times(1)).findByToken(token);
    }

    // 7. save()
    @Test
    void save_shouldCallUpdateUser() {
        User user = new User();

        userService.save(user);

        verify(userMapper, times(1)).updateUser(user);
    }
}
