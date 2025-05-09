package com.ureca.picky_be.base.business.notification;

import com.ureca.picky_be.base.business.notification.dto.CreateNotificationResp;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.entity.notification.NotificationType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationUseCase {

    
//    SseEmitter subscribe(Long senderId, String lastEventId);
    SseEmitter subscribe(String lastEventId);
    CreateNotificationResp send(Long receiverId, Long movieId, Long boardId);
    void sendAll(NotificationType notificationType, Long movieId, Long boardId);
    void sendNewBoardNotification(Long writerId, Long movieId, Long boardId);


    Slice<CreateNotificationResp> getUnreadNotifications(PageRequest pageRequest, Long lastNotificationId);

    SuccessCode updateNotificationToRead(Long notificationId);
}
