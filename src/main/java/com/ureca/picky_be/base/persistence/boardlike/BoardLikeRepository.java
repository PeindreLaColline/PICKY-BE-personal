package com.ureca.picky_be.base.persistence.boardlike;

import com.ureca.picky_be.jpa.board.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    int countByBoardId(Long boardId);
}
