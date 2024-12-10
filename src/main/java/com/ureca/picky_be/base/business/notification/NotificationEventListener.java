package com.ureca.picky_be.base.business.notification;

import com.ureca.picky_be.base.business.notification.dto.BoardCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;

    // 알림 전송은 비동기 처리로 진행
    @TransactionalEventListener
    @Async
    public void handleBoardCreatedEvent(BoardCreatedEvent event) {
        // sendTest 자체적으로 조회, 알림 전송(DB 저장)을 진행
        // TODO : 별도의 Transaction을 처리해야할 것인가 아닌가 고민 필요
        notificationService.sendTest(event.getMovieId(), event.getBoardId());
    }
}
