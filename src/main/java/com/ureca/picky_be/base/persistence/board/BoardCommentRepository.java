package com.ureca.picky_be.base.persistence.board;

import com.ureca.picky_be.jpa.entity.board.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByUserId(Long userId);

    @Modifying
    @Query("""
        DELETE FROM BoardComment bc WHERE bc.id = :id
    """)
    void deleteByBoardCommentId(@Param("id") Long boardCommentId);

    Integer countByBoardId(Long boardId);



}

