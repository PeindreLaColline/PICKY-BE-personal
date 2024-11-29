package com.ureca.picky_be.base.business.lineReview.dto;


public record CreateLineReviewLikeReq(Long lineReviewID, Long userId, String preference) {
}
