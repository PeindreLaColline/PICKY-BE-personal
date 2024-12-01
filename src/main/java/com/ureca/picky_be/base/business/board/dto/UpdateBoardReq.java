package com.ureca.picky_be.base.business.board.dto;

import java.util.List;

public record UpdateBoardReq(String boardContext, Long movieId, List<AddBoardContentReq> contents, boolean isSpoiler) {
}
