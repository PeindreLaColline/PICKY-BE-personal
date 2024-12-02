package com.ureca.picky_be.base.persistence.lineReview;

import com.ureca.picky_be.base.business.lineReview.dto.LineReviewProjection;
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

@Repository
public interface LineReviewRepository extends JpaRepository<LineReview, Long> {
    boolean existsByMovieIdAndUserId(Long movieId, Long userId);

    @Query("""
    SELECT lr.id AS id, lr.userId AS userId, lr.movieId AS movieId, lr.rating AS rating,
           lr.context AS context, lr.isSpoiler AS isSpoiler, COUNT(lrl.id) AS likes, lr.createdAt AS createdAt
    FROM LineReview lr
    LEFT JOIN LineReviewLike lrl ON lrl.lineReview.id = lr.id
    WHERE lr.movieId = :movieId
    GROUP BY lr.id
    HAVING (:lastReviewId IS NULL OR lr.id < :lastReviewId)
    ORDER BY likes DESC, lr.id DESC
""")
    Page<LineReviewProjection> findByMovieAndLikesCursor(
            @Param("movieId") Long movieId,
            @Param("lastReviewId") Long lastReviewId,
            Pageable pageable
    );

    @Query("""
    SELECT lr.id AS id, lr.userId AS userId, lr.movieId AS movieId, lr.rating AS rating,
           lr.context AS context, lr.isSpoiler AS isSpoiler, COUNT(lrl.id) AS likes, lr.createdAt AS createdAt
    FROM LineReview lr
    LEFT JOIN LineReviewLike lrl ON lrl.lineReview.id = lr.id
    WHERE lr.movieId = :movieId
    GROUP BY lr.id
    HAVING (:lastReviewId IS NULL OR lr.id < :lastReviewId)
    ORDER BY lr.createdAt DESC, lr.id DESC
""")
    Page<LineReviewProjection> findByMovieAndLatestCursor(
            @Param("movieId") Long movieId,
            @Param("lastReviewId") Long lastReviewId,
            Pageable pageable
    );



    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByUserId(Long userId);
}
