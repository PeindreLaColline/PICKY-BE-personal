package com.ureca.picky_be.base.business.lineReview.dto;

public record GetLineReviewGenderInfoResp(
        Integer totalCount,
        Integer manCount,
        Integer femaleCount,
        double manAverage,
        double femaleAverage,
        double totalAverage


) {
}
