package com.ureca.picky_be.base.persistence.lineReview;

import com.ureca.picky_be.jpa.entity.lineReview.LineReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface LineReviewLikeRepository extends JpaRepository<LineReviewLike, Long> {
    Optional<LineReviewLike> findByLineReviewIdAndUserId(Long lineReviewId, Long userId);

    @Query("SELECT COUNT(l) FROM LineReviewLike l WHERE l.lineReview.id = :lineReviewId AND l.preference = 'LIKE'")
    Integer countLikesByLineReviewId(@Param("lineReviewId") Long lineReviewId);

    @Query("SELECT COUNT(l) FROM LineReviewLike l WHERE l.lineReview.id = :lineReviewId AND l.preference = 'LIKE'")
    Integer countDisLikesByLineReviewId(@Param("lineReviewId") Long lineReviewId);

    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByUserId(Long userId);
}
