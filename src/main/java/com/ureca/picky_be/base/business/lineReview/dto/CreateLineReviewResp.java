package com.ureca.picky_be.base.business.lineReview.dto;

public record CreateLineReviewResp(Long userId, Long movieId, double rating, String context, Boolean isSpoiler) {
}
