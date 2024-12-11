package com.ureca.picky_be.base.business.notification.dto;

import lombok.Getter;

@Getter
public class BoardCreatedEvent {
    private final Long movieId;
    private final Long boardId;
    private final Long writerId;

    public BoardCreatedEvent(Long writerId, Long movieId, Long boardId) {
        this.writerId = writerId;
        this.movieId = movieId;
        this.boardId = boardId;
    }
}
