package com.example.auth_service.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Action {

    READ("read"),
    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete");

    private final String name;

}
