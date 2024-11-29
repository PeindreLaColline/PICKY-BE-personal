package com.ureca.picky_be.base.business.lineReview.dto;

public record CreateLineReviewReq(Long userId, Long movieId, double rating, String context, boolean isSpoiler ) {
}
