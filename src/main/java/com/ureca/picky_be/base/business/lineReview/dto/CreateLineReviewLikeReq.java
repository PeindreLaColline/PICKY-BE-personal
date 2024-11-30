package com.ureca.picky_be.base.business.lineReview.dto;


public record CreateLineReviewLikeReq(Long lineReviewId, Long userId, String preference) {
}
