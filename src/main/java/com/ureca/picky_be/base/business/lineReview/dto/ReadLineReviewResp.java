package com.ureca.picky_be.base.business.lineReview.dto;

import java.time.LocalDateTime;

public record ReadLineReviewResp(Long id,           // 한줄평 ID
                                 Long userId,       // 작성자 ID
                                 Long movieId,      // 영화 ID
                                 double rating,     // 평점
                                 String context,    // 한줄평 내용
                                 Boolean isSpoiler, // 스포일러 여부
                                 int likes,         // 좋아요 수
                                 LocalDateTime createdAt // 생성 시간
) {
}
