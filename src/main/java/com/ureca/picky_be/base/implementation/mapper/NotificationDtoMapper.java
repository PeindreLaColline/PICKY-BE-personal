package com.ureca.picky_be.base.implementation.mapper;

import com.ureca.picky_be.base.business.notification.dto.CreateNotificationResp;
import com.ureca.picky_be.base.business.notification.dto.NotificationProjection;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class NotificationDtoMapper {

    public CreateNotificationResp toCreateNotificationResp(NotificationProjection projection, Long notificationId) {
        try {
            return new CreateNotificationResp(
                    notificationId,
                    projection.getBoardId(),
                    projection.getMovieId(),
                    projection.getMovieTitle(),
                    projection.getMoviePosterUrl(),
                    projection.getSenderId(),
                    projection.getSenderProfileUrl(),
                    projection.getUserNickName(),
                    projection.getCreatedAt(),
                    Boolean.FALSE
            );
        } catch (Exception e) {
            throw new CustomException(ErrorCode.DTO_MAPPING_FAILED);
        }
    }

}
