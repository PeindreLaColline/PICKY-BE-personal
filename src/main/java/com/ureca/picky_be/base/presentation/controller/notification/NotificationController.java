package com.ureca.picky_be.base.presentation.controller.notification;


import com.ureca.picky_be.base.business.notification.NotificationUseCase;
import com.ureca.picky_be.base.business.notification.dto.CreateNotificationResp;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.entity.notification.NotificationType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {
    private final NotificationUseCase notificationUseCase;
    private final AuthManager authManager;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "SseEmitter 생성 및 연결", description = "사용자 로그인 시, SseEmitter 생성 혹은 연결하는 API")
    public SseEmitter connect(
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId
    ) {
        return notificationUseCase.subscribe(lastEventId);
    }

    @PostMapping("/alert")
    @Operation(summary = "알림 전송", description = "알림 전송하는 API")
    public CreateNotificationResp send(
            @RequestParam Long receiverId,
            @RequestParam Long boardId,
            @RequestParam Long movieId
            ) {
        return notificationUseCase.send(receiverId, movieId, boardId);
    }

    @PostMapping("/alert/all")
    @Operation(summary = "미구현!!! 모든 사용자한테 알림 전송(관리자용)", description = "혹시 몰라 모든 사용자에게 알림 전송하는 API(현재 그냥 SuccessCode return 함")
    public SuccessCode sendAll(
            @RequestParam Long boardId,
            @RequestParam Long movieId,
            @RequestParam Long writerId,
            @RequestParam(defaultValue = "") NotificationType type

    ) {
//        notificationUseCase.sendAll(NotificationType.LIKEMOVIENEWBOARD, 1L, 1L);
        return SuccessCode.NOTIFICATION_SENT_SUCCESS;
    }

    @PostMapping("/alert/test")
    @Operation(summary = "writerId 사용자가 boardId 게시글을 작성했을 때, 해당 movieId를 좋아요한 사람들에게 알림 보내는 API", description = "특정 영화에 대한 게시글을 작성하면 해당 영화를 좋아요 누른 사람들에게 알림 전송하는 API")
    public SuccessCode sendTest(
            @RequestParam Long boardId,
            @RequestParam Long movieId,
            @RequestParam Long writerId
    ) {
        notificationUseCase.sendTest(writerId, movieId, boardId);
        return SuccessCode.NOTIFICATION_SENT_SUCCESS;
    }


}

