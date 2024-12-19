package com.ureca.picky_be.jpa.entity.user;

import lombok.Getter;

@Getter
public enum Role {
    USER("user"),
    CRITIC("critic"),
    ADMIN("admin");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public static Role fromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Role value cannot be null or empty");
        }
        for (Role role : values()) {
            if (role.getValue().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + value);
    }
}
