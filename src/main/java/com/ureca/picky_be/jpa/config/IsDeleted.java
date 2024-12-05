package com.ureca.picky_be.jpa.config;

import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;

public enum IsDeleted {
    TRUE,
    FALSE;

    public static IsDeleted fromString(String value) {
        try {
            return IsDeleted.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_DELETED_TYPE);
        }
    }
}
