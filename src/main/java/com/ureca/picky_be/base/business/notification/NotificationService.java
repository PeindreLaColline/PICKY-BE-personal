package com.ureca.picky_be.base.business.notification;

import com.ureca.picky_be.base.business.notification.dto.CreateNotificationResp;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.notification.NotificationManager;
import com.ureca.picky_be.jpa.notification.NotificationType;
import com.ureca.picky_be.jpa.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;

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


        // 503 에러를 방지하기 위한 가짜 데이터 전송
        String eventId = notificationManager.makeTimeIncludeId(userId);
        notificationManager.sendNotification(emitter, eventId, emitterId, "EventStream Created. [UserId = %d]".formatted(userId));

        if(notificationManager.hasLostData(lastEventId)) {
            notificationManager.sendLostData(lastEventId, userId, emitterId, emitter);
        }

        return emitter;
    }

    @Override
    public CreateNotificationResp send(Long receiverId, Long movieId, Long boardId) {
        NotificationType type = NotificationType.LIKEMOVIENEWBOARD;
        return notificationManager.send(receiverId, type, boardId, movieId);
    }

    @Override
    public void sendAll(NotificationType notificationType, Long movieId, Long boardId) {
        notificationManager.sendAll(notificationType, movieId, boardId);
    }

    @Override
    public void sendTest(Long writerId, Long movieId, Long boardId){
        NotificationType type = NotificationType.LIKEMOVIENEWBOARD;
        List<User> users = notificationManager.sendTest(writerId, boardId);
        notificationManager.sendEmitter(users, writerId, movieId, boardId, type);
    }


}
