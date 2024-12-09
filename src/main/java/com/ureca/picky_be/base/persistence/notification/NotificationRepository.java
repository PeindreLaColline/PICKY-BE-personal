package com.ureca.picky_be.base.persistence.notification;

import com.ureca.picky_be.base.business.notification.dto.NotificationProjection;
import com.ureca.picky_be.jpa.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {


    @Query("""
    SELECT b.id AS boardId, m.id AS movieId, m.title AS movieTitle, m.posterUrl AS moviePosterUrl, u.id AS userId, u.profileUrl AS userProfileUrl, u.nickname AS userNickName, 
    b.createdAt AS createdAt 
    FROM Board b
    JOIN User u ON b.userId = u.id
    JOIN Movie m ON b.movie.id = m.id
    WHERE b.isDeleted = 'FALSE' AND m.isDeleted = 'FALSE' AND b.id = :boardId AND u.id = :userId AND m.id = :movieId
    """)
    NotificationProjection getNewBoardNotificationProjection(@Param("userId") Long userId, @Param("boardId") Long boardId, @Param("movieId") Long movieId);

}
