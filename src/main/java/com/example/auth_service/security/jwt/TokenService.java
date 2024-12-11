package com.example.auth_service.security.jwt;

import com.example.auth_service.model.entity.Token;
import com.example.auth_service.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository repository;

    public void save(Token token) {
        repository.save(token);
    }

    public void saveAll(List<Token> tokens) {
        repository.saveAll(tokens);
    }

    public Token findByToken(String jwt) {
        return repository.findByToken(jwt)
                .orElseThrow(() -> new RuntimeException("Not found token: " + jwt));
    }

    public List<Token> findAllValidTokensByUserId(Long userId) {
        return repository.findAllValidTokensByUserId(userId);
    }
}
