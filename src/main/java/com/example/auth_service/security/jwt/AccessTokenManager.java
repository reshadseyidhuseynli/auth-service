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
public class AccessTokenManager {

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

    public boolean isValid(String token, UserDetails userDetails) {
        boolean isExpired = extractExpirationFromAccessToken(token).before(new Date());
        return !isExpired && extractUsernameFromAccessToken(token).equals(userDetails.getUsername());
    }

    public String extractUsernameFromAccessToken(String token) {
        return extractClaimFromAccessToken(token, Claims::getSubject);
    }

    public Date extractExpirationFromAccessToken(String token) {
        return extractClaimFromAccessToken(token, Claims::getExpiration);
    }

    public <T> T extractClaimFromAccessToken(String token, Function<Claims, T> function) {
        Claims claims = extractAllClaimsFromAccessToken(token);
        return function.apply(claims);
    }

    public Claims extractAllClaimsFromAccessToken(String token) {
        return Jwts.parser()
                .verifyWith(jwtProperty.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
