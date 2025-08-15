package com.example.insurance_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .cors(withDefaults())
	        .csrf(csrf -> csrf.disable())
//	        .csrf(csrf -> csrf
//	                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//	            )
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(
	                "/api/auth/login",
	                "/api/auth/verify",
	                "/v3/api-docs/**",
	                "/swagger-ui.html",
	                "/swagger-ui/**"
	            ).permitAll()
	            .anyRequest().authenticated()
	        )
	        .formLogin(formLogin -> formLogin.disable())
	        .httpBasic(httpBasic -> httpBasic.disable());

	    return http.build();
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
