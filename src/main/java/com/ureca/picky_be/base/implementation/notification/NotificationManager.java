package com.ureca.picky_be.base.implementation.notification;

import com.ureca.picky_be.base.business.notification.dto.NotificationProjection;
import com.ureca.picky_be.base.implementation.mapper.NotificationDtoMapper;
import com.ureca.picky_be.base.persistence.movie.MovieRepository;
import com.ureca.picky_be.base.persistence.notification.EmitterRepository;
import com.ureca.picky_be.base.persistence.notification.NotificationRepository;
import com.ureca.picky_be.base.persistence.user.UserRepository;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.jpa.movie.Movie;
import com.ureca.picky_be.jpa.notification.Notification;
import com.ureca.picky_be.jpa.notification.NotificationType;
import com.ureca.picky_be.jpa.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.yaml.snakeyaml.emitter.Emitter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationManager {
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final UserRepository userRepository;
    private final NotificationDtoMapper notificationDtoMapper;
    private final Long timeoutMillis = 10_000L;
    private final MovieRepository movieRepository;


    public String makeTimeIncludeId(Long id) {
        return id + "_" + System.currentTimeMillis();
    }


    public SseEmitter createNewSseEmitter(String emitterId) {
        SseEmitter emitter = new SseEmitter(timeoutMillis);
        emitterRepository.save(emitterId, emitter);
        return emitter;
    }


    public void deleteEmitterDueToComplete(String emitterId) {
        emitterRepository.deleteById(emitterId);
    }

    public void deleteEmitterDueToTimeout(String emitterId) {
        emitterRepository.deleteById(emitterId);
    }

    public void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    public boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    public void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheByUserId(String.valueOf(userId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    public NotificationProjection getNewBoardNotificationData(Long senderId, Long boardId, Long movieId) {

        return notificationRepository.getNewBoardNotificationProjection(senderId, boardId, movieId);
    }

    public void send(Long senderId, Long receiverId, NotificationType notificationType, Long boardId, Long movieId) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        Notification notification = createNotification(receiver, movieId, boardId, notificationType);

        // TODO : 이 메소드는 게시글 생성에 대한 알림용 메소드, 차 컨트롤러에서도 호출함
        // TODO : 전송하려고 하는 User 엔티티 receiver를 받아 해당 사용자한테 알림을 보내는 것임
        // TODO : 해당 메소드 이외에 팔로우-팔로잉 알림 등 다른 알림들에 대해서도 별도의 메소드로 생성해야함.

        Long notificationId = notification.getId();
        String eventId = makeTimeIncludeId(receiver.getId());

        // 특정 사용자에 대한 emitter를 찾아오는 것
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterByUserId(String.valueOf(receiverId));

        // 여기에서 사용자 id(User receiver), 게시글 id(BoardId), 영화 Id(movieId)를 활용해 NotificaitonProjection으로 생성해야함.
        NotificationProjection noti = getNewBoardNotificationData(senderId, boardId, movieId);

        emitters.forEach(
                (id, emitter) -> {
                    emitterRepository.saveEventCache(id, notification);
                    sendNotification(emitter, eventId, id,
                            notificationDtoMapper.toCreateNotificationResp(noti, notificationId));
                }
        );
    }

    public Notification createNotification(User receiver, Long movieId, Long boardId,  NotificationType notificationType) {
        Notification noti = Notification.of(receiver, movieId, boardId, notificationType, Boolean.FALSE);
        return notificationRepository.save(noti);
    }


}
