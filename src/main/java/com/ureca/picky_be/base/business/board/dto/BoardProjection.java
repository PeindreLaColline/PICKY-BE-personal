package com.ureca.picky_be.base.business.board.dto;

import com.ureca.picky_be.base.business.board.dto.contentDto.GetBoardContentResp;

import java.time.LocalDateTime;
import java.util.List;
public interface BoardProjection {

    Long getBoardId();   // Board ID
    Long getWriterId(); // 작성자 ID
    String getWriterNickname(); // 작성자 닉네임
    String getWriterProfileUrl();   // 작성자 프로필 url
    String getContext(); // Board 글 내용
    Boolean getIsSpoiler(); // Board 스포일러 여부
    LocalDateTime getCreatedAt(); // Board 생성일자
    LocalDateTime getUpdatedAt(); // Board 업데이트 일자
    Integer getLikeCount(); // Board 좋아요 갯수
    Integer getCommentCount();  // Board 댓글 갯수
    String getMovieName(); // Board 영화 이름
    Boolean getIsLike();    // Board 사용자가 좋아요 눌렀는지 여부
}
