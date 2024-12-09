package com.example.auth_service.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/super-admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {

    @GetMapping
    @PreAuthorize("hasAuthority('super-admin:read')")
    public String getMethod() {
        return "get super-admin";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('super-admin:insert')")
    public String postMethod() {
        return "insert super-admin";
    }

    @PutMapping
    @PreAuthorize("hasAuthority('super-admin:update')")
    public String putMethod() {
        return "update super-admin";
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('super-admin:delete')")
    public String deleteMethod() {
        return "delete super-admin";
    }
}
