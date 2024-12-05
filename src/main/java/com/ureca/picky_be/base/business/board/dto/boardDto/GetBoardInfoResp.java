package com.ureca.picky_be.base.business.board.dto.boardDto;
import com.ureca.picky_be.base.business.board.dto.BoardContentPOJO;

import java.time.LocalDateTime;
import java.util.*;

public record GetBoardInfoResp(
        Long boardId,
        Long writerId,
        String writerNickname,
        String writerProfileUrl,
        String context,
        Boolean isSpoiler,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,
        Integer likesCount,
        Integer commentsCount,
        List<BoardContentPOJO> contents,
        String movieTitle,
        Boolean isLike) {

}
