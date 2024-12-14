package com.ureca.picky_be.base.business.board.dto.commentDto;

import java.time.LocalDateTime;

public record GetAllBoardCommentsResp(
        Long commentId,
        Long writerId,
        String writerNickname,
        String writerProfileUrl,
        String context,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,
        boolean isAuthor
) {
}
