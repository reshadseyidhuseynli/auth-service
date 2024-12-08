package com.example.auth_service.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

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

    public Claims extractAllClaimsFromAccessToken(String token) {
        return Jwts.parser()
                .verifyWith(jwtProperty.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
