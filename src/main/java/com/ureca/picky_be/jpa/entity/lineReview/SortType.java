package com.ureca.picky_be.jpa.entity.lineReview;

import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;

public enum SortType {
    LIKES,
    LATEST;

    public static SortType from(String sortName) {
        return switch (sortName.toUpperCase()) {
            case "likes" ->  LIKES;
            case "latest" -> LATEST;
            default -> throw new CustomException(ErrorCode.LINEREVIEW_INVALID_SORTTYPE);
        };
    }
}
