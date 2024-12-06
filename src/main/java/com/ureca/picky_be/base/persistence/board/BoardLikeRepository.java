package com.ureca.picky_be.base.persistence.board;

import com.ureca.picky_be.jpa.board.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByUserId(Long userId);
    Integer countByBoardId(Long boardId);

    @Query("SELECT bl FROM BoardLike bl WHERE bl.board.id = :boardId AND bl.userId = :userId")
    Optional<BoardLike> findByBoardIdAndUserId(@Param("boardId") Long boardId, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM BoardLike bl WHERE bl.board.id = :boardId AND bl.userId= :userId")
    void deleteByBoardIdAndUserId(Long boardId, Long userId);


}
