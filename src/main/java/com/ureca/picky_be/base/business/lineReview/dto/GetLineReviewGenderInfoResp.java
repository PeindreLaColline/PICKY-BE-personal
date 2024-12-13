package com.ureca.picky_be.base.business.lineReview.dto;

public record GetLineReviewGenderInfoResp(
        Integer totalCount,
        Integer maleCount,
        Integer femaleCount,
        double manAverage,
        double womanAverage
) {
}
