package com.example.auth_service.service;

import com.example.auth_service.model.dto.request.LoginRequest;
import com.example.auth_service.model.dto.request.RefreshTokenRequest;
import com.example.auth_service.model.dto.request.SignUpRequest;
import com.example.auth_service.model.dto.response.LoginResponse;
import com.example.auth_service.model.entity.Token;
import com.example.auth_service.model.entity.User;
import com.example.auth_service.security.jwt.AccessTokenManager;
import com.example.auth_service.security.jwt.RefreshTokenService;
import com.example.auth_service.security.jwt.TokenService;
import io.jsonwebtoken.Claims;
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

    private final AccessTokenManager accessTokenManager;
    private final RefreshTokenService refreshTokenService;
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
        return saveTokenAndPrepareLoginResponse(user, false);
    }

    public LoginResponse login(LoginRequest request) {
        authenticate(request);
        User user = userService.findByEmail(request.getUsername());
        invalidateOldTokens(user.getId());
        return saveTokenAndPrepareLoginResponse(user, request.getRememberMe());
    }

    public LoginResponse refreshToken(RefreshTokenRequest request) {
        Claims claims = refreshTokenService.extractAllClaimsFromRefreshToken(request.getAccessToken());
        String username = claims.getSubject();
        User user = userService.findByEmail(username);
        return saveTokenAndPrepareLoginResponse(user, request.getRememberMe());

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

    private LoginResponse saveTokenAndPrepareLoginResponse(User user, boolean rememberMe) {
        String accessToken = accessTokenManager.generateAccessToken(user);
        String refreshToken = refreshTokenService.generateRefreshToken(user, rememberMe);

        saveToken(accessToken, user);
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
