package com.example.auth_service.controller;

import com.example.auth_service.model.dto.request.ChangePasswordRequest;
import com.example.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/change-password")
    public void changePassword(ChangePasswordRequest request, Principal principal) {
        userService.changePassword(request, principal);
    }
}
