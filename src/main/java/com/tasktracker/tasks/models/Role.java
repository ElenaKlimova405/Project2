package com.tasktracker.tasks.models;

import org.springframework.security.core.GrantedAuthority;


public enum Role implements GrantedAuthority {
    USER("Пользователь"),
    ADMINISTRATOR("Администратор"),
    PROGRAMMER("Программист");

    private String enumName;

    Role(String enumName) {
        this.enumName = enumName;
    }

    public String getEnumName() {
        return enumName;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
