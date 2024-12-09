package com.ureca.picky_be.base.business.notification.dto;

import java.time.LocalDateTime;

public record CreateNotificationResp(Long notificationId, Long boardId, Long movieId, Long movieTitle, Long moviePosterUrl, Long userId, Long userProfileUrl, Long userNickName, LocalDateTime createdAt, Boolean isRead) {
}
