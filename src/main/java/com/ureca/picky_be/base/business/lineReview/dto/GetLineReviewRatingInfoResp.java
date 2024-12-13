package com.ureca.picky_be.base.business.lineReview.dto;

public record GetLineReviewRatingInfoResp(
        Integer totalCount,
        Integer oneCount,
        Integer twoCount,
        Integer threeCount,
        Integer fourCount,
        Integer fiveCount
) {
}
