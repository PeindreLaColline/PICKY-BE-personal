package com.ureca.picky_be.base.persistence.boardcomment;

import com.ureca.picky_be.jpa.board.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    int countByBoardId(Long boardId);
}
