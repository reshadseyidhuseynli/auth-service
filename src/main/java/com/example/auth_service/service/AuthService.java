package com.example.auth_service.service;

import com.example.auth_service.model.dto.request.LoginRequest;
import com.example.auth_service.model.dto.request.RefreshTokenRequest;
import com.example.auth_service.model.dto.request.SignUpRequest;
import com.example.auth_service.model.dto.response.LoginResponse;
import com.example.auth_service.security.jwt.AccessTokenService;
import com.example.auth_service.security.jwt.RefreshTokenService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final AuthenticationManager authManager;
    private final PasswordEncoder encoder;

    public LoginResponse login(LoginRequest request) {
        authenticate(request);
        return prepareLoginResponse(request.getUsername(), request.getRememberMe());
    }

    public void signUp(SignUpRequest request) {
        if (userService.checkExistUserByEmail(request.getEmail())) {
            throw new RuntimeException("Account already created with: " + request.getEmail());
        }
        request.setPassword(encoder.encode(request.getPassword()));
        userService.createUser(request);
    }

    public LoginResponse refreshToken(RefreshTokenRequest request) {
        Claims claims = refreshTokenService.extractAllClaimsFromRefreshToken(request.getAccessToken());
        String username = claims.getSubject();
        return prepareLoginResponse(username, request.getRememberMe());

    }

    private void authenticate(LoginRequest request) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword());
            authManager.authenticate(authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private LoginResponse prepareLoginResponse(String username, boolean rememberMe) {
        UserDetails userDetails = userService.loadUserByUsername(username);
        return LoginResponse.builder()
                .accessToken(accessTokenService.generateAccessToken(userDetails))
                .refreshToken(refreshTokenService.generateRefreshToken(userDetails, rememberMe))
                .build();
    }
}
