package com.ureca.picky_be.base.business.board.dto;
import java.util.List;

public record AddBoardReq(String boardContext, Long movieId, List<AddBoardContentReq> contents, boolean isSpoiler) {
}
