package com.ureca.picky_be.base.persistence.lineReview;

import com.ureca.picky_be.jpa.lineReview.LineReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LineReviewRepository extends JpaRepository<LineReview, Long> {
    boolean existsByMovieIdAndUserId(Long movieId, Long userId);

    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByUserId(Long userId);
}
