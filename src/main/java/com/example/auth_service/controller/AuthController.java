package com.example.auth_service.controller;

import com.example.auth_service.model.dto.request.LoginRequest;
import com.example.auth_service.model.dto.request.SignUpRequest;
import com.example.auth_service.model.dto.response.LoginResponse;
import com.example.auth_service.service.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@PermitAll
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public LoginResponse signUp(@RequestBody SignUpRequest request) {
        return authService.signUp(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("refresh-token")
    public LoginResponse refreshToken(HttpServletRequest request) {
        return authService.refreshToken(request);
    }

}
