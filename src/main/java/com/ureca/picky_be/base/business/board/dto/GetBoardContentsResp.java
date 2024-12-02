package com.ureca.picky_be.base.business.board.dto;

import com.ureca.picky_be.jpa.board.Board;
import com.ureca.picky_be.jpa.board.BoardContent;
import java.util.List;

public record GetBoardContentsResp(String contentUrl, String contentType) {

}
