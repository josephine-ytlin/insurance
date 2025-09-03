package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired 
	private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String token, String origin) {
        String subject = "驗證你的帳號";
        String url = origin + "/verify?token=" + token; // 或 Spring 提供的後端 /verify API
        String content = "請點擊以下連結完成驗證: " + url;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

}
