package com.example.auth_service.repository;

import com.example.auth_service.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String jwt);

    @Query("select t from Token t join t.user u where u.id = :userId and t.isValid = true")
    List<Token> findAllValidTokensByUserId(Long userId);

}