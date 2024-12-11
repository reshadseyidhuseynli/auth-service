package com.example.auth_service.service;

import com.example.auth_service.mapper.UserMapper;
import com.example.auth_service.model.dto.request.SignUpRequest;
import com.example.auth_service.model.entity.User;
import com.example.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User findByEmail(String username) {
        return repository.findByEmail(username);
    }

    public boolean checkExistUserByEmail(String email) {
        return repository.existsUserByEmail(email);
    }

    public User createUser(SignUpRequest request) {
        return repository.save(UserMapper.INSTANCE.mapToUser(request));
    }
}
