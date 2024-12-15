package com.ureca.picky_be.jpa.entity.board;

import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;

public enum BoardContentType {
    IMAGE,
    VIDEO;

    public static BoardContentType fromString(String value) {
        try {
            return BoardContentType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_CONTENT_TYPE);
        }
    }
}
