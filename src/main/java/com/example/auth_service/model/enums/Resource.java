package com.example.auth_service.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Resource {

    AUTH("auth"),
    SUPER_ADMIN("super-admin"),
    ADMIN("admin");

    private final String name;

}
