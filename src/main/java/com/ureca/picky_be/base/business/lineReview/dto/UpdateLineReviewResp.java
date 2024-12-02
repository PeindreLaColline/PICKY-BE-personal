package com.ureca.picky_be.base.business.lineReview.dto;

public record UpdateLineReviewResp(Long Id ,Long userId, Long movieId, double rating, String context, Boolean isSpoiler) {
}
