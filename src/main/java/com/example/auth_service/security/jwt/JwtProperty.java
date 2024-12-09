package com.example.auth_service.security.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Data
@Configuration
@ConfigurationProperties("security.jwt")
public class JwtProperty {

    private String secretKey;
    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;
    private SecretKey key;

    @PostConstruct
    private void setKey() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

}