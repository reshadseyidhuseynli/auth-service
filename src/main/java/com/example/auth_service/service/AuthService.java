package com.example.auth_service.service;

import com.example.auth_service.model.dto.request.LoginRequest;
import com.example.auth_service.model.dto.request.SignUpRequest;
import com.example.auth_service.model.dto.response.LoginResponse;
import com.example.auth_service.model.entity.Token;
import com.example.auth_service.model.entity.User;
import com.example.auth_service.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final TokenService tokenService;
    private final UserService userService;
    private final AuthenticationManager authManager;
    private final PasswordEncoder encoder;

    public LoginResponse signUp(SignUpRequest request) {
        if (userService.checkExistUserByEmail(request.getEmail())) {
            throw new RuntimeException("Account already created with: " + request.getEmail());
        }

        request.setPassword(encoder.encode(request.getPassword()));
        User user = userService.createUser(request);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user, false);

        saveToken(accessToken, user);

        return prepareLoginResponse(accessToken, refreshToken);
    }

    public LoginResponse login(LoginRequest request) {
        authenticate(request);
        User user = userService.findByEmail(request.getUsername());
        invalidateOldTokens(user.getId());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user, request.getRememberMe());

        saveToken(accessToken, user);

        return prepareLoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refreshToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Refresh token doesn't added");
        }
        final String refreshToken = header.substring(7);

        String username = jwtService.extractUsernameFromToken(refreshToken);
        User user = userService.findByEmail(username);
        jwtService.checkTokenValidity(refreshToken, user);

        String accessToken = jwtService.generateAccessToken(user);
        invalidateOldTokens(user.getId());
        saveToken(accessToken, user);
        return prepareLoginResponse(accessToken,refreshToken);
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

    private LoginResponse prepareLoginResponse(String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveToken(String jwt, User user) {
        Token token = Token.builder()
                .token(jwt)
                .user(user)
                .isValid(true)
                .build();
        tokenService.save(token);
    }

    private void invalidateOldTokens(Long userId) {
        List<Token> tokens = tokenService.findAllValidTokensByUserId(userId);
        tokens.forEach(token -> {
            token.setIsValid(false);
        });
        tokenService.saveAll(tokens);
    }
}
