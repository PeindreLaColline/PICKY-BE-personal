package com.ureca.picky_be.jpa.user;

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
}
