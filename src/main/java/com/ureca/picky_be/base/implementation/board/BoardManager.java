package com.ureca.picky_be.base.implementation.board;

import com.ureca.picky_be.base.business.board.dto.AddBoardReq;
import com.ureca.picky_be.jpa.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardManager {

    public Board createBoardWithContents(Long userId, AddBoardReq req) {
        Board board = Board.builder()


    }
}
