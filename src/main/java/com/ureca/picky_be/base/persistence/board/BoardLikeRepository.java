package com.ureca.picky_be.base.persistence.board;

import com.ureca.picky_be.jpa.board.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByUserId(Long userId);
    int countByBoardId(Long boardId);
}
