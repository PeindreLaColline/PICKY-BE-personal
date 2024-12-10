package com.ureca.picky_be.base.business.board.dto.boardDto;
import com.ureca.picky_be.base.business.board.dto.contentDto.AddBoardContentReq;

import java.util.List;

public record AddBoardReq(String boardContext, Long movieId, boolean isSpoiler) {
}
