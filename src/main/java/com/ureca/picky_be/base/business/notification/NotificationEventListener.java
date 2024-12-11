package com.ureca.picky_be.base.business.notification;

import com.ureca.picky_be.base.business.notification.dto.BoardCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {
    private final NotificationService notificationService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 이벤트 리스너가 발행된 트랜잭션과 별도로 실행되도록
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    @EventListener
    public void handleBoardCreatedEvent(BoardCreatedEvent event) {
        try {
            log.info("Event Listener Start");
            notificationService.sendTest(event.getWriterId(), event.getMovieId(), event.getBoardId());
            log.info("Event Listener End");
        } catch (Exception e) {
            System.out.println("Event Listener Error = " + e.getMessage());
        }

    }
    // 알림 전송은 비동기 처리로 진행
}
