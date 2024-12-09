package com.example.auth_service.model.dto.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;
    private Boolean rememberMe;

}
