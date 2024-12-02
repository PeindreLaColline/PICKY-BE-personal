package com.ureca.picky_be.base.persistence.board;

import com.ureca.picky_be.jpa.board.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByUserId(Long userId);
    Integer countByBoardId(Long boardId);
}

