package com.example.auth_service.model.dto.request;

import lombok.Data;

@Data
public class SignUpRequest {

    private String name;
    private String surname;
    private String email;
    private String password;

}
