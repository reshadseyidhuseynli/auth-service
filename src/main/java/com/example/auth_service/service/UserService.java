package com.example.auth_service.service;

import com.example.auth_service.mapper.UserMapper;
import com.example.auth_service.model.dto.request.ChangePasswordRequest;
import com.example.auth_service.model.dto.request.SignUpRequest;
import com.example.auth_service.model.entity.User;
import com.example.auth_service.model.enums.DefaultRoles;
import com.example.auth_service.model.enums.UserStatus;
import com.example.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public User findByEmail(String username) {
        return repository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Not found user with: " + username));
    }

    public boolean checkExistUserByEmail(String email) {
        return repository.existsUserByEmail(email);
    }

    public User createUser(SignUpRequest request) {
        User entity = UserMapper.INSTANCE.mapToUser(request);
        entity.setRole(roleService.getRoleByName(DefaultRoles.ROLE_USER));
        entity.setStatus(UserStatus.ACTIVE);
        return repository.save(entity);
    }

    public void changePassword(ChangePasswordRequest request, Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Password isn't correct");
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())){
            throw new RuntimeException("Passwords aren't same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
    }
}
