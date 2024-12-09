package com.ureca.picky_be.base.implementation.mapper;

import com.ureca.picky_be.base.business.notification.dto.CreateNotificationResp;
import com.ureca.picky_be.base.business.notification.dto.NotificationProjection;
import org.springframework.stereotype.Component;

@Component
public class NotificationDtoMapper {

    public CreateNotificationResp toCreateNotificationResp(NotificationProjection projection, Long notificationId) {
        return new CreateNotificationResp(
                notificationId,
                projection.getBoardId(),
                projection.getMovieId(),
                projection.getMovieTitle(),
                projection.getMoviePosterUrl(),
                projection.getUserId(),
                projection.getUserProfileUrl(),
                projection.getUserNickName(),
                projection.getCreatedAt(),
                Boolean.FALSE
        );

    }

}
