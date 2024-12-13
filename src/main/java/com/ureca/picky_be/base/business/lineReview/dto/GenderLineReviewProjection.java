package com.ureca.picky_be.base.business.lineReview.dto;

public interface GenderLineReviewProjection {
    Integer getTotalCount();
    Integer getMaleCount();
    Integer getFemaleCount();
    double getMaleAverageRating();
    double getFemaleAverageRating();
}
