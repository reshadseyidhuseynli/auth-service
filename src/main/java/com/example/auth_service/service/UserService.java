package com.example.auth_service.service;

import com.example.auth_service.model.dto.request.SignUpRequest;
import com.example.auth_service.mapper.UserMapper;
import com.example.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }

    public boolean checkExistUserByEmail(String email) {
        return repository.existsUserByUsername(email);
    }

    public void createUser(SignUpRequest request) {
        repository.save(UserMapper.INSTANCE.mapToUser(request));
    }
}
