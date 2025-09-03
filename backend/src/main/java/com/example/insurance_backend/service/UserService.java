package com.example.insurance_backend.service;

import com.example.insurance_backend.mapper.UserMapper;
import com.example.insurance_backend.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

	private final UserMapper userMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String username, String password, String origin) {
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(hashedPassword);

        String token = UUID.randomUUID().toString(); //唯一性高＆不可預測＆方便
        user.setVerificationToken(token);
        user.setVerified(false);

        userMapper.insertUser(user);

        // 寄送驗證信
        emailService.sendVerificationEmail(username, token, origin);
    }

    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) return null;
//        if (!user.isVerified()) return null; // 必須先驗證
        if (passwordEncoder.matches(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public User findByToken(String token) {
        return userMapper.findByToken(token);
    }

    public void save(User user) {
        userMapper.updateUser(user);
    }
}
