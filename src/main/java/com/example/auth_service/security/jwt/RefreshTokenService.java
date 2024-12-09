package com.example.auth_service.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtProperty jwtProperty;

    public String generateRefreshToken(UserDetails user, boolean rememberMe) {
        Date now = new Date();
        long refreshTokenExpiration = rememberMe ? jwtProperty.getRefreshTokenExpiration() * 30L : jwtProperty.getRefreshTokenExpiration();
        Date exp = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .subject(String.valueOf(user.getUsername()))
                .issuedAt(now)
                .expiration(exp)
                .claim("type", "REFRESH_TOKEN")
                .signWith(jwtProperty.getKey())
                .compact();
    }

    public Claims extractAllClaimsFromRefreshToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(jwtProperty.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        if (!claims.get("type").equals("REFRESH_TOKEN")) {
            throw new RuntimeException("Token is not correct");
        }
        return claims;
    }

}
