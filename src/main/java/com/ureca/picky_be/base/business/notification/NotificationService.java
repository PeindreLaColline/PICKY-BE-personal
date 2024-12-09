package com.ureca.picky_be.base.business.notification;

import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.notification.NotificationManager;
import com.ureca.picky_be.jpa.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationUseCase {
    private final NotificationManager notificationManager;
    private final AuthManager authManager;

    @Override
//    public SseEmitter subscribe(Long userId, String lastEventId) {
    public SseEmitter subscribe(String lastEventId) {
        Long userId = authManager.getUserId();
        String emitterId = notificationManager.makeTimeIncludeId(userId);
        SseEmitter emitter = notificationManager.createNewSseEmitter(emitterId);
        emitter.onCompletion(() -> notificationManager.deleteEmitterDueToComplete(emitterId));
        emitter.onTimeout(() -> notificationManager.deleteEmitterDueToTimeout(emitterId));

        String eventId = notificationManager.makeTimeIncludeId(userId);

        notificationManager.sendNotification(emitter, eventId, emitterId, "EventStream Created. [UserId = %d]".formatted(userId));

        if(notificationManager.hasLostData(lastEventId)) {
            notificationManager.sendLostData(lastEventId, userId, emitterId, emitter);
        }

        return emitter;
    }

    @Override
    public void send(Long writerId, Long receiverId, NotificationType notificationType, Long movieId, Long boardId) {

        notificationManager.send(writerId, receiverId, notificationType, movieId, boardId);
    }
}
