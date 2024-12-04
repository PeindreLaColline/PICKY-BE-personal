package com.ureca.picky_be.base.business.lineReview.dto;

public record UpdateLineReviewResp(Long Id, String writerNickname, Long userId, Long movieId, double rating, String context, Boolean isSpoiler) {
}
