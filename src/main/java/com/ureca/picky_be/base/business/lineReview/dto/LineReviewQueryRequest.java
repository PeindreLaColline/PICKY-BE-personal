package com.ureca.picky_be.base.business.lineReview.dto;

import com.ureca.picky_be.jpa.lineReview.SortType;

import java.time.LocalDateTime;

public record LineReviewQueryRequest(Long movieId,            // 영화 ID
                                     Long lastReviewId,       // 마지막 한줄평 ID (커서)
                                     LocalDateTime lastCreatedAt, // 마지막 생성 시간 (커서)
                                     SortType sortType        // 정렬 방식 (LATEST 또는 LIKES)) {
){

}
