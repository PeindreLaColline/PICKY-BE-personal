package com.ureca.picky_be.base.persistence;

import com.ureca.picky_be.jpa.board.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    void deleteByUserId(@Param("userId") Long userId);
}

