package com.ureca.picky_be.base.persistence.board;

import com.ureca.picky_be.jpa.board.Board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

//    @EntityGraph(attributePaths = {"board_content"})
//    @Query(value = "SELECT * FROM Board b WHERE b.movie.id = :movieId AND b.id > :currentId ORDER BY b.created_at DESC LIMIT 10 OFFSET 0", nativeQuery = true)
//    List<Board> getRecentMovieRelatedBoards(@Param("movieId") Long movieId, @Param("currentId") Long currentId);
//
//    @Query("SELECT b.id AS b.id, b.context, FROM Board b WHERE b.movie.id = :movieId AND b.id > :currentId ORDER BY b.created_at DESC LIMIT 10 OFFSET 0")
//    List<Integer> getRecentMovieRelatedBoardsIds(@Param("movieId") Long movieId, @Param("currentId") Long currentId);


}
