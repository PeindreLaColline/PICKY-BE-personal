package com.ureca.picky_be.base.persistence.board;

import com.ureca.picky_be.base.business.board.dto.BoardCommentProjection;
import com.ureca.picky_be.base.business.board.dto.BoardProjection;
import com.ureca.picky_be.jpa.entity.board.Board;
import com.ureca.picky_be.jpa.entity.config.IsDeleted;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByUserId(Long userId);

    Optional<Board> findById(Long id);

    @ReadOnlyProperty
    @Query("""
        SELECT b.isDeleted as isDeleted
        FROM Board b
        WHERE b.id = :boardId
    """)
    IsDeleted findIsDeleted(@Param("boardId") Long boardId);

    @Query("""
    SELECT b.id AS boardId, b.userId AS writerId, b.writerNickname AS writerNickname, u.profileUrl AS writerProfileUrl, b.context AS context, b.isSpoiler AS isSpoiler,
        b.createdAt AS createdAt, b.updatedAt AS updatedAt,
        (SELECT COUNT(l) FROM BoardLike l WHERE l.board.id = b.id) AS likeCount,
        (SELECT COUNT(c) FROM BoardComment c WHERE c.board.id = b.id) AS commentCount,
        (CASE WHEN EXISTS (SELECT 1 FROM BoardLike bl WHERE bl.board.id = b.id AND bl.userId = :userId) THEN true ELSE false END) AS isLike,
        m.title AS movieName,
        (CASE WHEN b.userId = :userId THEN true ELSE false END) AS isAuthor
    FROM Board b
    JOIN User u ON b.userId = u.id
    JOIN Movie m ON b.movie.id = m.id
    WHERE b.movie.id = :movieId AND b.isDeleted = 'FALSE' AND (:lastBoardId IS NULL OR b.id < :lastBoardId)
    ORDER BY b.createdAt DESC
    """)
    Slice<BoardProjection> getRecentMovieRelatedBoards(@Param("userId") Long userId, @Param("movieId") Long movieId, @Param("lastBoardId") Long lastBoardId, Pageable pageable);


    @Query("""
    SELECT b.id AS boardId, b.userId AS writerId, b.writerNickname AS writerNickname, u.profileUrl AS writerProfileUrl, b.context AS context, b.isSpoiler AS isSpoiler,
        b.createdAt AS createdAt, b.updatedAt AS updatedAt,
        (SELECT COUNT(l) FROM BoardLike l WHERE l.board.id = b.id) AS likeCount,
        (SELECT COUNT(c) FROM BoardComment c WHERE c.board.id = b.id) AS commentCount,
        (CASE WHEN EXISTS (SELECT 1 FROM BoardLike bl WHERE bl.board.id = b.id AND bl.userId = :userId) THEN true ELSE false END) AS isLike,
        (SELECT COALESCE(JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'contentUrl', bc.contentUrl,
                        'boardContentType', bc.boardContentType
                         )
                    ), '[]')
            FROM BoardContent bc
            WHERE bc.board.id = b.id) AS contents,
        m.title AS movieName,
        (CASE WHEN b.userId = :userId THEN true ELSE false END) AS isAuthor
    FROM Board b
    JOIN User u ON b.userId = u.id
    JOIN Movie m ON b.movie.id = m.id
    WHERE b.isDeleted = 'FALSE' AND (:lastBoardId IS NULL OR b.id < :lastBoardId)
    ORDER BY b.createdAt DESC
    """)
    Slice<BoardProjection> getRecentBoards(@Param("userId") Long userId, @Param("lastBoardId") Long lastBoardId, Pageable pageable);

    @Query("""
    SELECT bc.id as commentId, bc.userId AS writerId, u.nickname AS writerNickname, u.profileUrl AS writerProfileUrl, bc.context AS context,
        bc.createdAt AS createdAt, bc.updatedAt AS updatedAt,
        (CASE WHEN bc.userId = :userId THEN true ELSE false END) AS isAuthor
    FROM BoardComment bc
    JOIN User u ON bc.userId = u.id
    WHERE bc.board.id = :boardId and bc.board.isDeleted = 'FALSE' AND (:lastCommentId IS NULL OR bc.id < :lastCommentId)
    ORDER BY bc.createdAt DESC
    """)
    Slice<BoardCommentProjection> getBoardComments(@Param("userId") Long userId, @Param("boardId") Long boardId, @Param("lastCommentId") Long lastCommentId, Pageable pageable);

    @Query("""
    SELECT b.id AS boardId, b.userId AS writerId, b.writerNickname AS writerNickname, u.profileUrl AS writerProfileUrl, b.context AS context, b.isSpoiler AS isSpoiler,
        b.createdAt AS createdAt, b.updatedAt AS updatedAt,
        (SELECT COUNT(l) FROM BoardLike l WHERE l.board.id = b.id) AS likeCount,
        (SELECT COUNT(c) FROM BoardComment c WHERE c.board.id = b.id) AS commentCount,
        (CASE WHEN EXISTS (SELECT 1 FROM BoardLike bl WHERE bl.board.id = b.id AND bl.userId = :userId) THEN true ELSE false END) AS isLike,
        m.title AS movieName,
        (CASE WHEN b.userId = :userId THEN true ELSE false END) AS isAuthor
    FROM Board b
    JOIN User u ON b.userId = u.id
    JOIN Movie m ON b.movie.id = m.id
    WHERE b.userId = :userId AND b.isDeleted = 'FALSE' AND (:lastBoardId IS NULL OR b.id < :lastBoardId)
    ORDER BY b.createdAt DESC
    """)
    Slice<BoardProjection> findByIdAndCursor(@Param("userId") Long userId, @Param("lastBoardId") Long lastBoardId, Pageable pageable);

    Integer countByUserId(Long userId);



}
