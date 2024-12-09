package com.example.auth_service.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public String getMethod() {
        return "get admin";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:insert')")
    public String postMethod() {
        return "insert admin";
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    public String putMethod() {
        return "update admin";
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
    public String deleteMethod() {
        return "delete admin";
    }

}
