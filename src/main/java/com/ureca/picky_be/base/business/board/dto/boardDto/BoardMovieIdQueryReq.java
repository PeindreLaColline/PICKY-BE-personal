package com.ureca.picky_be.base.business.board.dto.boardDto;

public record BoardMovieIdQueryReq(
        Long movieId,
        Long lastBoardId
) {
}
