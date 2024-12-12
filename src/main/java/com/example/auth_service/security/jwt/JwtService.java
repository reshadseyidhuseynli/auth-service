package com.example.auth_service.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperty jwtProperty;

    public String generateAccessToken(UserDetails user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtProperty.getAccessTokenExpiration());

        return Jwts.builder()
                .subject(String.valueOf(user.getUsername()))
                .issuedAt(now)
                .expiration(exp)
                .signWith(jwtProperty.getKey())
                .compact();
    }

    public String generateRefreshToken(UserDetails user, boolean rememberMe) {
        Date now = new Date();
        long exp = rememberMe ? jwtProperty.getRefreshTokenExpiration() * jwtProperty.getRememberMeCoefficient()
                : jwtProperty.getRefreshTokenExpiration();
        Date expirationDate = new Date(now.getTime() + exp);

        return Jwts.builder()
                .subject(String.valueOf(user.getUsername()))
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(jwtProperty.getKey())
                .compact();
    }

    public void checkTokenValidity(String token, UserDetails userDetails) {
        if (extractExpirationFromToken(token).before(new Date())) {
            throw new RuntimeException("The token is expired");
        }
        if (!extractUsernameFromToken(token).equals(userDetails.getUsername())) {
            throw new RuntimeException("The token is invalid");
        }
    }

    public String extractUsernameFromToken(String token) {
        String username = extractClaimFromToken(token, Claims::getSubject);
        if (username == null) {
            throw new RuntimeException("Could not get username from token");
        }
        return username;
    }

    public Date extractExpirationFromToken(String token) {
        return extractClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T extractClaimFromToken(String token, Function<Claims, T> function) {
        Claims claims = extractAllClaimsFromToken(token);
        return function.apply(claims);
    }

    public Claims extractAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(jwtProperty.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
