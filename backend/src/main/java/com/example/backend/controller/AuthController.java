package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.LoginRequest;
import com.example.backend.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    

	public AuthController(AuthService authService) {
		super();
		this.authService = authService;
	}

	@PostMapping("/login")
    @Operation(summary = "登入或註冊")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest req, HttpSession session, HttpServletRequest httpRequest) {
        ApiResponse<String> response = authService.loginOrRegister(req, session, httpRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    @Operation(summary = "驗證 Email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam String token) {
        ApiResponse<Void> response = authService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "登出")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        ApiResponse<Void> response = authService.logout(session);
        return ResponseEntity.ok(response);
    }
}
