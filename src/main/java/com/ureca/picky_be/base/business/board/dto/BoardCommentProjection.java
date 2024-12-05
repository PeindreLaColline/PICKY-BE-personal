package com.ureca.picky_be.base.business.board.dto;

import java.time.LocalDateTime;

public interface BoardCommentProjection {
    Long getCommentId();
    Long getWriterId(); // 작성자 ID
    String getWriterNickname(); // 작성자 닉네임
    String getWriterProfileUrl();   // 작성자 프로필 url
    String getContext(); // 글 내용
    LocalDateTime getCreatedAt(); // 생성일자
    LocalDateTime getUpdatedAt(); // 업데이트 일자
}

