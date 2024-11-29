package com.ureca.picky_be.base.persistence.boardcontent;

import com.ureca.picky_be.jpa.board.BoardContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardContentRepository extends JpaRepository<BoardContent, Long> {
}
