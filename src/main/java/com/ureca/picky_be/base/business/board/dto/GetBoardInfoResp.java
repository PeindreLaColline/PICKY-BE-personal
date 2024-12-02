package com.ureca.picky_be.base.business.board.dto;
import java.util.*;

public record GetBoardInfoResp(
        Long boardId,
        Long movieId,
        String context,
        List<GetBoardContentsResp> contents,
        Integer likesCount,
        Integer commentsCount,
        boolean isSpoiler,
        boolean isLike) {

}
