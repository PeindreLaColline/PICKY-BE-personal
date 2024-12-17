package com.ureca.picky_be.base.persistence.notification;

import com.ureca.picky_be.base.business.notification.dto.CreateNotificationResp;
import com.ureca.picky_be.base.business.notification.dto.NotificationProjection;
import com.ureca.picky_be.jpa.entity.notification.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {


    @Query("""
    SELECT b.id AS boardId, m.id AS movieId, m.title AS movieTitle, m.posterUrl AS moviePosterUrl, u.id AS senderId, u.profileUrl AS senderProfileUrl, u.nickname AS senderNickname,
    b.createdAt AS createdAt
    FROM Board b
    JOIN User u ON b.userId = u.id
    JOIN Movie m ON b.movie.id = m.id
    WHERE b.isDeleted = 'FALSE' AND m.isDeleted = 'FALSE' AND b.id = :boardId AND u.id = :senderId AND m.id = :movieId
    """)
    NotificationProjection getNewBoardNotificationProjection(@Param("senderId") Long senderId, @Param("boardId") Long boardId, @Param("movieId") Long movieId);



    @Query("""
        SELECT new com.ureca.picky_be.base.business.notification.dto.CreateNotificationResp(
                   n.id,
                   n.boardId,
                   n.movieId,
                   m.title,
                   m.posterUrl,
                   b.userId,
                   u.profileUrl,
                   u.nickname,
                   n.createdAt,
                   n.isRead
               )
        FROM Notification n
        LEFT JOIN Movie m ON m.id = n.movieId
        LEFT JOIN Board b ON b.id = n.boardId
        LEFT JOIN User u ON u.id = b.userId
        WHERE n.receiver.id = :userId AND (:lastNotificationId IS NULL OR n.id < :lastNotificationId)
          AND n.isRead = false
        ORDER BY n.createdAt DESC
    """)
    Slice<CreateNotificationResp> findUnreadNotificationsByUserId(
            @Param("userId") Long userId,
            @Param("lastNotificationId") Long lastNotificationId,
            Pageable pageable
    );

}
