package com.ureca.picky_be.base.persistence.lineReview;

import com.ureca.picky_be.jpa.lineReview.LineReview;
import com.ureca.picky_be.jpa.lineReview.LineReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LineReviewLikeRepository extends JpaRepository<LineReviewLike, Long> {
    Optional<LineReviewLike> findByLineReviewIdAndUserId(Long lineReviewId, Long userId);

    @Query("SELECT COUNT(l) FROM LineReviewLike l WHERE l.lineReview.id = :lineReviewId AND l.preference = 'LIKE'")
    Integer countLikesByLineReviewId(@Param("lineReviewId") Long lineReviewId);

    @Query("SELECT COUNT(l) FROM LineReviewLike l WHERE l.lineReview.id = :lineReviewId AND l.preference = 'LIKE'")
    Integer countDisLikesByLineReviewId(@Param("lineReviewId") Long lineReviewId);
}
