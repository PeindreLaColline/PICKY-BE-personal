package com.ureca.picky_be.base.business.notification;

import com.ureca.picky_be.base.business.notification.dto.CreateNotificationResp;
import com.ureca.picky_be.jpa.notification.NotificationType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationUseCase {

    
//    SseEmitter subscribe(Long userId, String lastEventId);
    SseEmitter subscribe(String lastEventId);
    CreateNotificationResp send(Long receiverId, Long movieId, Long boardId);
    void sendAll(NotificationType notificationType, Long movieId, Long boardId);

    void sendTest(Long writerId, Long movieId, Long boardId);
}
