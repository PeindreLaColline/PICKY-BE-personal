package com.ureca.picky_be.base.business.board.dto;

import com.ureca.picky_be.jpa.board.BoardContentType;

public record AddBoardContentReq(String contentUrl, BoardContentType type) {
}
