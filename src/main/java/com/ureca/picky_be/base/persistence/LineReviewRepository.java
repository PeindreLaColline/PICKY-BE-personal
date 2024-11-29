package com.ureca.picky_be.base.persistence;

import com.ureca.picky_be.jpa.linereview.LineReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineReviewRepository extends JpaRepository<LineReview, Long> {
    void deleteByUserId(Long userId);
}
