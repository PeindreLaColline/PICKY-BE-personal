package com.ureca.picky_be.base.persistence.board;

import com.ureca.picky_be.base.business.board.dto.BoardCommentProjection;
import com.ureca.picky_be.base.business.board.dto.BoardProjection;
import com.ureca.picky_be.base.business.board.dto.commentDto.GetAllBoardCommentsResp;
import com.ureca.picky_be.jpa.board.Board;
import com.ureca.picky_be.jpa.board.BoardContent;
import com.ureca.picky_be.jpa.config.IsDeleted;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByUserId(Long userId);

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
        (CASE WHEN EXISTS (SELECT 1 FROM BoardLike bl WHERE bl.board.id = b.id AND bl.user.id = :userId) THEN true ELSE false END) AS isLike,
        (SELECT COALESCE(JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'contentUrl', bc.contentUrl,
                        'boardContentType', bc.boardContentType
                         )
                    ), '[]')
            FROM BoardContent bc
            WHERE bc.board.id = b.id) AS contents,
        m.title AS movieName
    FROM Board b
    JOIN User u ON b.userId = u.id
    JOIN Movie m ON b.movie.id = m.id
    WHERE b.movie.id = :movieId AND b.isDeleted = 'FALSE'
    ORDER BY b.createdAt DESC
    """)
    Slice<BoardProjection> getRecentMovieRelatedBoards(@Param("userId") Long userId, @Param("movieId") Long movieId, Pageable pageable);


    @Query("""
    SELECT b.id AS boardId, b.userId AS writerId, b.writerNickname AS writerNickname, u.profileUrl AS writerProfileUrl, b.context AS context, b.isSpoiler AS isSpoiler,
        b.createdAt AS createdAt, b.updatedAt AS updatedAt,
        (SELECT COUNT(l) FROM BoardLike l WHERE l.board.id = b.id) AS likeCount,
        (SELECT COUNT(c) FROM BoardComment c WHERE c.board.id = b.id) AS commentCount,
        (CASE WHEN EXISTS (SELECT 1 FROM BoardLike bl WHERE bl.board.id = b.id AND bl.user.id = :userId) THEN true ELSE false END) AS isLike,
        (SELECT COALESCE(JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'contentUrl', bc.contentUrl,
                        'boardContentType', bc.boardContentType
                         )
                    ), '[]')
            FROM BoardContent bc
            WHERE bc.board.id = b.id) AS contents,
        m.title AS movieName
    FROM Board b
    JOIN User u ON b.userId = u.id
    JOIN Movie m ON b.movie.id = m.id
    WHERE b.isDeleted = 'FALSE'
    ORDER BY b.createdAt DESC
    """)
    Slice<BoardProjection> getRecentBoards(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT bc.id as commentId, bc.userId AS writerId, u.name AS writerNickname, u.profileUrl AS writerProfileUrl, bc.context AS context,
        bc.createdAt AS createdAt, bc.updatedAt AS updatedAt
    FROM BoardComment bc
    JOIN User u ON bc.userId = u.id
    WHERE bc.board.id = :boardId and bc.board.isDeleted = 'FALSE'
    ORDER BY bc.createdAt DESC
    """)
    Slice<BoardCommentProjection> getBoardComments(@Param("boardId") Long boardId, Pageable pageable);



}
