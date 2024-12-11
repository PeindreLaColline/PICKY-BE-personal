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
import com.ureca.picky_be.jpa.board.Board;
import com.ureca.picky_be.jpa.movie.Movie;
import com.ureca.picky_be.jpa.movie.MovieLike;
import com.ureca.picky_be.jpa.notification.Notification;
import com.ureca.picky_be.jpa.notification.NotificationType;
import com.ureca.picky_be.jpa.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.yaml.snakeyaml.emitter.Emitter;
import java.util.List;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationManager {
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final UserRepository userRepository;
    private final NotificationDtoMapper notificationDtoMapper;
//    private final Long timeoutMillis = 10_000L;
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
            System.out.println("Notification sent to " + emitterId);
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
        User receiver = userRepository.findById(senderId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        Notification notification = createNotification(receiver, movieId, boardId, notificationType);

        // TODO : 이 메소드는 게시글 생성에 대한 알림용 메소드, 타 컨트롤러에서도 호출함
        // TODO : 전송하려고 하는 User 엔티티 receiver를 받아 해당 사용자한테 알림을 보내는 것임
        // TODO : 해당 메소드 이외에 팔로우-팔로잉 알림 등 다른 알림들에 대해서도 별도의 메소드로 생성해야함.

        Long notificationId = notification.getId();
        String eventId = makeTimeIncludeId(receiver.getId());

        // 특정 사용자에 대한 emitter를 찾아오는 것
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterByUserId(String.valueOf(receiverId));

        NotificationProjection noti = getNewBoardNotificationData(senderId, boardId, movieId);
        CreateNotificationResp data = notificationDtoMapper.toCreateNotificationResp(noti, notificationId);
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

            Long notificationId = notification.getId();
            String eventId = makeTimeIncludeId(receiver.getId());

            // 특정 사용자에 대한 emitter를 찾아오는 것
            Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterByUserId(String.valueOf(receiver.getId()));

            // 여기에서 사용자 id(User receiver), 게시글 id(BoardId), 영화 Id(movieId)를 활용해 NotificaitonProjection으로 생성해야함.
            NotificationProjection noti = getNewBoardNotificationData(null, boardId, movieId);

            CreateNotificationResp data = notificationDtoMapper.toCreateNotificationResp(noti, notificationId);
            emitters.forEach(
                    (id, emitter) -> {
                        emitterRepository.saveEventCache(id, notification);
                        sendNotification(emitter, eventId, id,
                                data);
                    }
            );
        }


    }

    public List<Long> getMovieLikeUsers(Long movieId) {
        return movieLikeRepository.findUserIdsByMovieId(movieId);
    }

    public Notification createNotification(User receiver, Long movieId, Long boardId,  NotificationType notificationType) {
        Notification noti = Notification.of(receiver, movieId, boardId, notificationType, Boolean.FALSE);
        return notificationRepository.save(noti);
    }


    @Transactional(readOnly = true)
    public List<User> sendTest(Long senderId, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Movie movie = board.getMovie();

        List<Long> userIds = getMovieLikeUsers(movie.getId());
        return userRepository.findAllById(userIds);
    }

    @Transactional
    public void sendEmitter(List<User> receivers, Long senderId, Long movieId, Long boardId, NotificationType type) {
        for(User receiver : receivers) {
            try {
                Notification notification = createNotification(receiver, movieId, boardId, type);

                Long notificationId = notification.getId();
                String eventId = makeTimeIncludeId(receiver.getId());

                // 특정 사용자에 대한 emitter를 찾아오는 것
                Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterByUserId(String.valueOf(receiver.getId()));

                // 게시글 작성자 NotificationProjection 생성
                NotificationProjection noti = getNewBoardNotificationData(senderId, boardId, movieId);

                log.info("특정 게시물에 대한 알림 전송 : " + boardId);
                CreateNotificationResp data = notificationDtoMapper.toCreateNotificationResp(noti, notificationId);
                emitters.forEach(
                        (id, emitter) -> {
                            try {
                                emitterRepository.saveEventCache(id, notification);
                                sendNotification(emitter, eventId, id, data);
                            } catch (Exception e) {
                                System.err.println("알림 전송 실패 Emitter ID " + id + ": " + e.getMessage());
                            }
                        }
                );
            } catch(Exception e) {
                System.err.println("알림 전송 실패 사용자 ID " + receiver.getId() + ": " + e.getMessage());
            }
        }
    }
}
