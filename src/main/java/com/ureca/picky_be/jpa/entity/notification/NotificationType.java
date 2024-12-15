package com.ureca.picky_be.jpa.entity.notification;

import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;

public enum NotificationType {
    LIKEMOVIENEWBOARD,
    FOLLOWINGNEWBOARD;
//    MYBOARDNEWCOMMENT;        // 보류, 내 게시글에 댓글이 달리는 케이스는 별도 엔티티 고려필요

    public static NotificationType fromString(String value) {
        try {
            return NotificationType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_NOTIFICATION_TYPE);
        }
    }


}
