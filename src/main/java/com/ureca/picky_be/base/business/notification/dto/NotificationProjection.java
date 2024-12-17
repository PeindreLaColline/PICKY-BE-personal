package com.ureca.picky_be.base.business.notification.dto;

import java.time.LocalDateTime;

public interface NotificationProjection {
    Long getBoardId();
    Long getMovieId();
    String getMovieTitle();
    String getMoviePosterUrl();
    Long getSenderId();
    String getSenderProfileUrl();
    String getSenderNickname();
    LocalDateTime getCreatedAt();
//    Boolean getIsRead();
}
