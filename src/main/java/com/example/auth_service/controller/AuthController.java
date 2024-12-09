package com.example.auth_service.controller;

import com.example.auth_service.model.dto.request.LoginRequest;
import com.example.auth_service.model.dto.request.RefreshTokenRequest;
import com.example.auth_service.model.dto.request.SignUpRequest;
import com.example.auth_service.model.dto.response.LoginResponse;
import com.example.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody SignUpRequest request) {
        authService.signUp(request);
    }

    @PostMapping("refresh-token")
    public LoginResponse refreshToken(@RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }

}
