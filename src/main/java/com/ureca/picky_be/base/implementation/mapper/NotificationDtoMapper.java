package com.ureca.picky_be.base.implementation.mapper;

import com.ureca.picky_be.base.business.notification.dto.CreateNotificationResp;
import com.ureca.picky_be.base.business.notification.dto.NotificationProjection;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.jpa.entity.notification.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationDtoMapper {

    public CreateNotificationResp toCreateNotificationResp(NotificationProjection projection, Notification notification) {
        try {
            return new CreateNotificationResp(
                    notification.getId(),
                    projection.getBoardId(),
                    projection.getMovieId(),
                    projection.getMovieTitle(),
                    projection.getMoviePosterUrl(),
                    projection.getSenderId(),
                    projection.getSenderProfileUrl(),
                    projection.getSenderNickname(),
                    notification.getCreatedAt(),
                    Boolean.FALSE
            );
        } catch (Exception e) {
            throw new CustomException(ErrorCode.DTO_MAPPING_FAILED);
        }
    }

}
