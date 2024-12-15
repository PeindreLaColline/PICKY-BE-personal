package com.ureca.picky_be.base.business.board.dto.contentDto;

import com.ureca.picky_be.jpa.entity.board.BoardContentType;

public record BoardContentWithBoardId(
        Long boardId,
        String contentUrl,
        BoardContentType boardContentType
) {
}
