package com.ureca.picky_be.base.business.notification;

import com.ureca.picky_be.jpa.notification.NotificationType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationUseCase {

    
//    SseEmitter subscribe(Long userId, String lastEventId);
    SseEmitter subscribe(String lastEventId);
    void send(Long writerId, Long receiverId, NotificationType notificationType, Long movieId, Long boardId);
}
