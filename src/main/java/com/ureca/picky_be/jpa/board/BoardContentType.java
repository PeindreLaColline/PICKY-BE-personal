package com.ureca.picky_be.jpa.board;

import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.jpa.lineReview.Preference;

public enum BoardContentType {
    PHOTO,
    VIDEO;

    public static BoardContentType fromString(String value) {
        try {
            return BoardContentType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_CONTENT_TYPE);
        }
    }


}
