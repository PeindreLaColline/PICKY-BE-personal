package com.ureca.picky_be.base.presentation.controller.notification;


import com.ureca.picky_be.base.business.notification.NotificationUseCase;
import com.ureca.picky_be.base.business.notification.dto.CreateNotificationResp;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.notification.Notification;
import com.ureca.picky_be.jpa.notification.NotificationType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public CreateNotificationResp send(@RequestParam Long receiverId) {
        return notificationUseCase.send(receiverId);
    }

    @PostMapping("/alert/all")
    @Operation(summary = "모든 사용자한테 알림 전송", description = "알림 전송하는 API")
    public SuccessCode sendAll() {
        notificationUseCase.sendAll(NotificationType.LIKEMOVIENEWBOARD, 1L, 1L);
        return SuccessCode.NOTIFICATION_SENT_SUCCESS;
    }


}

