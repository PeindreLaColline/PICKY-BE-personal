package com.ureca.picky_be.base.business.notification.dto;

import java.time.LocalDateTime;

public record CreateNotificationResp(
        Long notificationId,
        Long boardId,
        Long movieId,
        String movieTitle,
        String moviePosterUrl,
        Long userId,
        String userProfileUrl,
        String userNickName,
        LocalDateTime createdAt,
        Boolean isRead
) {
}
