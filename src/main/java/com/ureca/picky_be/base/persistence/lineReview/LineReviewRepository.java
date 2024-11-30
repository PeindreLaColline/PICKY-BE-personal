package com.ureca.picky_be.base.persistence.lineReview;

import com.ureca.picky_be.jpa.lineReview.LineReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineReviewRepository extends JpaRepository<LineReview, Long> {
    boolean existsByMovieIdAndUserId(Long movieId, Long userId);
}
