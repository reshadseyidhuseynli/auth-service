package com.example.auth_service.repository;

import com.example.auth_service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    boolean existsUserByEmail(String email);
}
