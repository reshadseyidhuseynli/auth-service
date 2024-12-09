package com.example.auth_service.model.dto.request;

import lombok.Data;

@Data
public class RefreshTokenRequest {

    private String accessToken;
    private Boolean rememberMe;

}
