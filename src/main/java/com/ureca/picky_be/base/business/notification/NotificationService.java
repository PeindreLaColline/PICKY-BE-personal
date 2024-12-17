package com.ureca.picky_be.base.business.notification;

import com.ureca.picky_be.base.business.notification.dto.CreateNotificationResp;
import com.ureca.picky_be.base.business.notification.dto.NotificationProjection;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.content.ProfileManager;
import com.ureca.picky_be.base.implementation.notification.NotificationManager;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.entity.notification.NotificationType;
import com.ureca.picky_be.jpa.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService implements NotificationUseCase {
    private final NotificationManager notificationManager;
    private final AuthManager authManager;
    private final ProfileManager profileManager;

    @Override
    public SseEmitter subscribe(String lastEventId) {
        Long userId = authManager.getUserId();
        String emitterId = notificationManager.makeTimeIncludeId(userId);
        SseEmitter emitter = notificationManager.createNewSseEmitter(emitterId);
        emitter.onCompletion(() -> notificationManager.deleteEmitterDueToComplete(emitterId));
        emitter.onTimeout(() -> notificationManager.deleteEmitterDueToTimeout(emitterId));

        // 503 에러를 방지하기 위한 가짜 데이터 전송
        String eventId = notificationManager.makeTimeIncludeId(userId);
        notificationManager.sendNotification(emitter, eventId, emitterId, "EventStream Created.");

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
    public void sendNewBoardNotification(Long writerId, Long movieId, Long boardId) {
        NotificationType type = NotificationType.LIKEMOVIENEWBOARD;
        List<User> users = notificationManager.findMovieLikeUsers(writerId, boardId);
        List<CreateNotificationResp> notifications = users.stream()
                .map(receiver -> {
                    // NotificationProjection 조회
                    NotificationProjection noti = notificationManager.getNewBoardNotificationData(writerId, boardId, movieId);

                    // Notification 생성 및 ID 반환
                    Long notificationId = notificationManager.createNotification(receiver, movieId, boardId, type).getId();
                    String senderProfileUrl = profileManager.getPresignedUrl(noti.getSenderProfileUrl());
                    // CreateNotificationResp 객체 생성 및 URL 변환
                    return new CreateNotificationResp(
                            notificationId,
                            noti.getBoardId(),
                            noti.getMovieId(),
                            noti.getMovieTitle(),
                            noti.getMoviePosterUrl(),
                            noti.getSenderId(),
                            senderProfileUrl,
                            noti.getSenderNickname(),
                            noti.getCreatedAt(),
                            Boolean.FALSE
                    );
                })
                .toList();

        notificationManager.sendEmitter(users, notifications);
    }

    @Override
    public Slice<CreateNotificationResp> getUnreadNotifications(PageRequest pageRequest, Long lastNotificationId) {
        Long receiverId = authManager.getUserId();
        Slice<CreateNotificationResp> resp = notificationManager.getNotifications(receiverId, lastNotificationId, pageRequest);

        // TODO : 리팩토링
        return resp.map(this::convertProfileUrl);
    }

    private CreateNotificationResp convertProfileUrl(CreateNotificationResp noti) {
        String presignedUrl = profileManager.getPresignedUrl(noti.senderProfileUrl());
        return new CreateNotificationResp(
                noti.notificationId(),
                noti.boardId(),
                noti.movieId(),
                noti.movieTitle(),
                noti.moviePosterUrl(),
                noti.senderId(),
                presignedUrl,
                noti.senderNickname(),
                noti.createdAt(),
                noti.isRead()
        );
    }

    @Override
    public SuccessCode updateNotificationToRead(Long notificationId) {
        try {
            notificationManager.updateNotificationToRead(notificationId);
        } catch (Exception e) {
            System.out.println("e = " + e.getMessage());
            throw new CustomException(ErrorCode.NOTIFICATION_UPDATE_FAILED);
        }
        return SuccessCode.NOTIFICATION_READ_SUCCESS;
    }

}
