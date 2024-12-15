package com.ureca.picky_be.jpa.entity.lineReview;

import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;

public enum Preference {
    LIKE,
    DISLIKE;
    public static Preference fromString(String value) {
        try {
            return Preference.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_PREFERENCE);
        }
    }
}
