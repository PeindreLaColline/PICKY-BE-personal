package com.ureca.picky_be.base.business.notification.dto;

import java.time.LocalDateTime;

public interface NotificationProjection {
    Long getBoardId();
    Long getMovieId();
    Long getMovieTitle();
    Long getMoviePosterUrl();
    Long getUserId();
    Long getUserProfileUrl();
    Long getUserNickName();
    LocalDateTime getCreatedAt();
//    Boolean getIsRead();
}
