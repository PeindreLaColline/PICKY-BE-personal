package com.ureca.picky_be.jpa.entity.user;

import lombok.Getter;

@Getter
public enum Nationality {
    DOMESTIC("domestic"),
    INTERNATIONAL("international");

    private final String value;
    Nationality(String value) {
        this.value = value;
    }

}
