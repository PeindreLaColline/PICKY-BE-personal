package com.ureca.picky_be.base.business.lineReview.dto;

import java.time.LocalDateTime;

public interface LineReviewProjection {
    Long getId();            // 한줄평 ID
    Long getUserId();        // 작성자 ID
    Long getMovieId();       // 영화 ID
    double getRating();      // 평점
    String getContext();     // 한줄평 내용
    Boolean getIsSpoiler();     // 스포일러 여부
    Integer getLikes();
    Integer getDislikes();// 좋아요 수
    LocalDateTime getCreatedAt(); // 생성 시간
}
