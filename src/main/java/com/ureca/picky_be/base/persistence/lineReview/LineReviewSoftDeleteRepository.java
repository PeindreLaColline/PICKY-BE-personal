package com.ureca.picky_be.base.persistence.lineReview;

import com.ureca.picky_be.jpa.entity.lineReview.LineReviewSoftDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LineReviewSoftDeleteRepository extends JpaRepository<LineReviewSoftDelete, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByUserId(Long userId);
}

