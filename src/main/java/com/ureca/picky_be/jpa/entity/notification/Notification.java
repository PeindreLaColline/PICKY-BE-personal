package com.ureca.picky_be.jpa.entity.notification;

import com.ureca.picky_be.jpa.entity.config.BaseEntity;
import com.ureca.picky_be.jpa.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User receiver;

    @Column(nullable = false)
    private Long movieId;

    @Column(nullable = false)
    private Long boardId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Column(name = "is_read", nullable = false)   // TRUE, FALSE ENUM 타입인 알림 읽음 여부
    private Boolean isRead;


    public static Notification of(User receiver, Long movieId, Long boardId, NotificationType notificationType, Boolean isRead) {
        Notification noti = Notification.builder()
                .receiver(receiver)
                .movieId(movieId)
                .boardId(boardId)
                .notificationType(notificationType)
                .isRead(Boolean.FALSE)
                .build();
        return noti;
    }

    public void read() {
        this.isRead = Boolean.TRUE;
    }



}
