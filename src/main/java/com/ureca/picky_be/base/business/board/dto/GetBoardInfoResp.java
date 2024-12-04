package com.ureca.picky_be.base.business.board.dto;
import java.time.LocalDateTime;
import java.util.*;

public record GetBoardInfoResp(
        Long boardId,
        Long writerId,
        String writerNickname,
        String writerProfileUrl,
        String context,
        boolean isSpoiler,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,
        Integer likesCount,
        Integer commentsCount,
        List<GetBoardContentsResp> contents,
        String movieTitle,
        Boolean isLike) {

}
