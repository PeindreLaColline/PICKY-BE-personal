package com.ureca.picky_be.base.business.lineReview.dto;

public record CreateLineReviewReq(Long movieId, double rating, String context, Boolean isSpoiler ) {
}
