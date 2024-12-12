package com.ureca.picky_be.base.persistence.lineReview;

import com.ureca.picky_be.base.business.lineReview.dto.LineReviewProjection;
import com.ureca.picky_be.base.business.lineReview.dto.MyPageLineReviewProjection;
import com.ureca.picky_be.jpa.lineReview.LineReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface LineReviewRepository extends JpaRepository<LineReview, Long> {
    boolean existsByMovieIdAndUserId(Long movieId, Long userId);

    @Query("""
        SELECT lr.id AS id, lr.userId AS userId, lr.writerNickname AS writerNickname, lr.movieId AS movieId, lr.rating AS rating,
               lr.context AS context, lr.isSpoiler AS isSpoiler,
               COUNT(CASE WHEN lrl.preference = 'LIKE' AND lrl.isDeleted = false THEN lrl.id END) AS likes,
               COUNT(CASE WHEN lrl.preference = 'DISLIKE' AND lrl.isDeleted = false THEN lrl.id END) AS dislikes,
               lr.createdAt AS createdAt
        FROM LineReview lr
        LEFT JOIN LineReviewLike lrl ON lrl.lineReview.id = lr.id
        WHERE lr.movieId = :movieId
          AND (:lastReviewId IS NULL OR lr.id < :lastReviewId)
        GROUP BY lr.id, lr.userId, lr.movieId, lr.rating, lr.context, lr.isSpoiler, lr.createdAt
        ORDER BY likes DESC, lr.id DESC
    """)
    Slice<LineReviewProjection> findByMovieAndLikesCursor(
            @Param("movieId") Long movieId,
            @Param("lastReviewId") Long lastReviewId,
            Pageable pageable
    );

    @Query("""
        SELECT lr.id AS id, lr.userId AS userId,  lr.writerNickname AS writerNickname,lr.movieId AS movieId, lr.rating AS rating,
               lr.context AS context, lr.isSpoiler AS isSpoiler,
               COUNT(CASE WHEN lrl.preference = 'LIKE' AND lrl.isDeleted = false THEN lrl.id END) AS likes,
               COUNT(CASE WHEN lrl.preference = 'DISLIKE' AND lrl.isDeleted = false THEN lrl.id END) AS dislikes,
               lr.createdAt AS createdAt
        FROM LineReview lr
        LEFT JOIN LineReviewLike lrl ON lrl.lineReview.id = lr.id
        WHERE lr.movieId = :movieId
          AND (:lastCreatedAt IS NULL OR lr.createdAt < :lastCreatedAt
               OR (lr.createdAt = :lastCreatedAt AND lr.id < :lastReviewId))
        GROUP BY lr.id, lr.userId, lr.movieId, lr.rating, lr.context, lr.isSpoiler, lr.createdAt
        ORDER BY lr.createdAt DESC, lr.id DESC
""")
    Slice<LineReviewProjection> findByMovieAndLatestCursor(
            @Param("movieId") Long movieId,
            @Param("lastReviewId") Long lastReviewId,
            @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
            Pageable pageable
    );

    @Query("""
        SELECT lr.id AS id, lr.writerNickname AS writerNickname, lr.userId AS userId, lr.movieId AS movieId, m.title AS movieTitle, m.posterUrl AS moviePosterUrl, lr.rating AS rating,
               lr.context AS context, lr.isSpoiler AS isSpoiler,
               COUNT(CASE WHEN lrl.preference = 'LIKE' AND lrl.isDeleted = false THEN lrl.id END) AS likes,
               COUNT(CASE WHEN lrl.preference = 'DISLIKE' AND lrl.isDeleted = false THEN lrl.id END) AS dislikes,
               lr.createdAt AS createdAt
        FROM LineReview lr
        LEFT JOIN LineReviewLike lrl ON lrl.lineReview.id = lr.id
        LEFT JOIN Movie m ON lr.movieId = m.id
        WHERE lr.userId = :userId AND lr.id > :lastReviewId
        GROUP BY lr.id, lr.userId, lr.movieId, lr.rating, lr.context, lr.isSpoiler, lr.createdAt
        ORDER BY lr.createdAt DESC
""")
    Slice<MyPageLineReviewProjection> findByUserIdAndCursor(
            @Param("userId") Long userId,
            @Param("lastReviewId") Long lastReviewId,
            Pageable pageable
    );


    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByUserId(Long userId);
}
