package com.ureca.picky_be.jpa.notification;

import com.ureca.picky_be.jpa.config.BaseEntity;
import com.ureca.picky_be.jpa.config.IsDeleted;
import com.ureca.picky_be.jpa.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE )
//    @JoinColumn(name="user_id")
    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false)
    private Long movieId;

    @Column(nullable = false)
    private Long boardId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Column(name = "is_read", nullable = false)   // TRUE, FALSE ENUM 타입인 알림 읽음 여부
    private IsDeleted isRead;






}
