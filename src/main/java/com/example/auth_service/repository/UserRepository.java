package com.example.auth_service.repository;

import com.example.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String email);

    boolean existsUserByUsername(String email);
}
