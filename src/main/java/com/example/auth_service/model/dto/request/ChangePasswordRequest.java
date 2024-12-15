package com.example.auth_service.model.dto.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {

    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;

}
