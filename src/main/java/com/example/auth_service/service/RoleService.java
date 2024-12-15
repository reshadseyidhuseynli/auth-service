package com.example.auth_service.service;

import com.example.auth_service.model.entity.Role;
import com.example.auth_service.model.enums.DefaultRoles;
import com.example.auth_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository repository;

    public Role getRoleByName(DefaultRoles role) {
        return repository.findByName(role.name())
                .orElseThrow(() -> new RuntimeException("It doesn't available like: " + role.name()));
    }
}
