package com.ureca.picky_be.base.implementation.notification;

import com.ureca.picky_be.base.business.notification.dto.CreateNotificationResp;
import com.ureca.picky_be.base.business.notification.dto.NotificationProjection;
import com.ureca.picky_be.base.implementation.mapper.NotificationDtoMapper;
import com.ureca.picky_be.base.persistence.board.BoardRepository;
import com.ureca.picky_be.base.persistence.movie.MovieLikeRepository;
import com.ureca.picky_be.base.persistence.movie.MovieRepository;
import com.ureca.picky_be.base.persistence.notification.EmitterRepository;
import com.ureca.picky_be.base.persistence.notification.NotificationRepository;
import com.ureca.picky_be.base.persistence.user.UserRepository;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.jpa.entity.board.Board;
import com.ureca.picky_be.jpa.entity.movie.Movie;
import com.ureca.picky_be.jpa.entity.notification.Notification;
import com.ureca.picky_be.jpa.entity.notification.NotificationType;
import com.ureca.picky_be.jpa.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationManager {
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final UserRepository userRepository;
    private final NotificationDtoMapper notificationDtoMapper;
    private final Long timeoutMillis = 600_000L;
    private final MovieRepository movieRepository;
    private final BoardRepository boardRepository;
    private final MovieLikeRepository movieLikeRepository;
    private final ApplicationEventPublisher eventPublisher;

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

    @Transactional
    public void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheByUserId(String.valueOf(userId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }


    public NotificationProjection getNewBoardNotificationData(Long senderId, Long boardId, Long movieId) {
        return notificationRepository.getNewBoardNotificationProjection(senderId, boardId, movieId);
    }

    @Transactional
    public CreateNotificationResp send(Long receiverId, NotificationType notificationType, Long boardId, Long movieId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        Long senderId = board.getUserId();
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        Notification notification = createNotification(receiver, movieId, boardId, notificationType);

        String eventId = makeTimeIncludeId(receiver.getId());

        // 특정 사용자에 대한 emitter를 찾아오는 것
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterByUserId(String.valueOf(receiverId));

        NotificationProjection noti = getNewBoardNotificationData(senderId, boardId, movieId);

        CreateNotificationResp data = notificationDtoMapper.toCreateNotificationResp(noti, notification);
        emitters.forEach(
                (id, emitter) -> {
                    emitterRepository.saveEventCache(id, notification);
                    sendNotification(emitter, eventId, id, data);
                }
        );
        return data;

    }



    @Transactional
    public void sendAll(NotificationType notificationType, Long boardId, Long movieId) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new CustomException(ErrorCode.NO_USER_FOUND);
        }

        for(User receiver : users) {
            Notification notification = createNotification(receiver, movieId, boardId, notificationType);
            String eventId = makeTimeIncludeId(receiver.getId());

            // 특정 사용자에 대한 emitter를 찾아오는 것
            Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterByUserId(String.valueOf(receiver.getId()));

            // 여기에서 사용자 id(User receiver), 게시글 id(BoardId), 영화 Id(movieId)를 활용해 NotificaitonProjection으로 생성해야함.
            NotificationProjection noti = getNewBoardNotificationData(null, boardId, movieId);

            CreateNotificationResp data = notificationDtoMapper.toCreateNotificationResp(noti, notification);
            emitters.forEach(
                    (id, emitter) -> {
                        emitterRepository.saveEventCache(id, notification);
                        sendNotification(emitter, eventId, id,
                                data);
                    }
            );
        }
    }

    public List<Long> getMovieLikeUsers(Long movieId, Long currentUserId) {
        return movieLikeRepository.findUserIdsByMovieId(movieId, currentUserId);
    }

    public Notification createNotification(User receiver, Long movieId, Long boardId,  NotificationType notificationType) {
        Notification noti = Notification.of(receiver, movieId, boardId, notificationType, Boolean.FALSE);
        return notificationRepository.save(noti);
    }


    @Transactional(readOnly = true)
    public List<User> findMovieLikeUsers(Long senderId, Long boardId, Long currentUserId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Movie movie = board.getMovie();

        List<Long> userIds = getMovieLikeUsers(movie.getId(), currentUserId);
        return userRepository.findAllById(userIds);
    }

    @Transactional
    public void sendEmitter(List<User> receivers, List<CreateNotificationResp> resps) {
        for (int i = 0; i < receivers.size(); i++) {
            User receiver = receivers.get(i);
            CreateNotificationResp data = resps.get(i);
            log.info(data.toString());
            Optional<Notification> notification = notificationRepository.findById(data.notificationId());
            try {
                String eventId = makeTimeIncludeId(receiver.getId());
                // 특정 사용자에 대한 emitter를 찾아오기
                Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterByUserId(String.valueOf(receiver.getId()));

                log.info("알림 전송: Board ID - {}, Receiver ID - {}", data.boardId(), receiver.getId());
                emitters.forEach((id, emitter) -> {
                    try {
                        // Event Cache 저장 및 Emitter 전송
                        if(notification.isPresent()) emitterRepository.saveEventCache(id, notification);
                        sendNotification(emitter, eventId, id, data);
                    } catch (Exception e) {
                        log.error("Emitter 전송 실패: Emitter ID - {}", id, e);
                    }
                });
            } catch (Exception e) {
                log.error("알림 전송 실패: Receiver ID - {}", receiver.getId(), e);
            }
        }

    }

    @Transactional(readOnly = true)
    public Slice<CreateNotificationResp> getNotifications(Long receiverId, Long lastNotificationId, Pageable pageable) {
        lastNotificationIdValidation(lastNotificationId);
        return notificationRepository.findUnreadNotificationsByUserId(receiverId, lastNotificationId, pageable);
    }

    private void lastNotificationIdValidation(Long lastNotificationId) {
        if(lastNotificationId == null) return;
        if(lastNotificationId <= 0) {
            throw new CustomException(ErrorCode.LAST_ID_INVALID_CURSOR);
        }
    }

    @Transactional
    public void updateNotificationToRead(Long notificationId) {
        Notification noti = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
        if(!noti.getIsRead()) {
            noti.read();
            notificationRepository.save(noti);
        }
    }
}
