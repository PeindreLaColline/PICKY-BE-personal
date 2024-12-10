package com.ureca.picky_be.base.business.notification.dto;

import lombok.Getter;

@Getter
public class BoardCreatedEvent {
    private final Long movieId;
    private final Long boardId;


    public BoardCreatedEvent(Long movieId, Long boardId) {
        this.movieId = movieId;
        this.boardId = boardId;
    }
}
