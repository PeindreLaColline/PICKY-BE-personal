package com.ureca.picky_be.base.implementation.board;

import com.ureca.picky_be.base.business.board.dto.BoardCommentProjection;
import com.ureca.picky_be.base.business.board.dto.boardDto.AddBoardReq;
import com.ureca.picky_be.base.business.board.dto.BoardProjection;
import com.ureca.picky_be.base.business.board.dto.boardDto.UpdateBoardReq;
import com.ureca.picky_be.base.business.board.dto.contentDto.AddBoardContentReq;
import com.ureca.picky_be.base.business.board.dto.contentDto.BoardContentWithBoardId;
import com.ureca.picky_be.base.business.user.dto.BoardQueryReq;
import com.ureca.picky_be.base.persistence.board.BoardLikeRepository;
import com.ureca.picky_be.base.persistence.board.BoardRepository;
import com.ureca.picky_be.base.persistence.board.BoardCommentRepository;
import com.ureca.picky_be.base.persistence.board.BoardContentRepository;
import com.ureca.picky_be.base.persistence.movie.MovieRepository;
import com.ureca.picky_be.base.persistence.user.UserRepository;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.jpa.board.Board;
import com.ureca.picky_be.jpa.board.BoardComment;
import com.ureca.picky_be.jpa.board.BoardLike;
import com.ureca.picky_be.jpa.config.IsDeleted;
import com.ureca.picky_be.jpa.movie.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class BoardManager {
    private final BoardRepository boardRepository;
    private final MovieRepository movieRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardContentRepository boardContentRepository;
    private final UserRepository userRepository;


    @Transactional
    public Board addBoard(Long userId, String userNickname, AddBoardReq addBoardReq, List<AddBoardContentReq> addBoardContentReqs) {
        Movie movie = movieRepository.findById(addBoardReq.movieId())
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        try {
            Board board = Board.of(userId, movie, addBoardReq.boardContext(), addBoardReq.isSpoiler(), addBoardContentReqs, userNickname);
            boardRepository.save(board);
            return board;
        } catch (CustomException e) {
            throw new CustomException(ErrorCode.BOARD_CREATE_FAILED);
        }
    }

    @Transactional
    public void updateBoard(Long boardId, Long userId, UpdateBoardReq req) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        if(board.getIsDeleted() == IsDeleted.TRUE) throw new CustomException(ErrorCode.BOARD_IS_DELETED);
        try {
            board.updateBoard(req.boardContext(), req.isSpoiler());
            boardRepository.save(board);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BOARD_UPDATE_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public Slice<BoardProjection> getRecentMovieRelatedBoards(Long userId, Long movieId, Long lastBoardId, Pageable pageable) {
        // 특정 영화 무비로그들 최신순 기준으로 Board들을 가져온다
        if(!movieRepository.existsById(movieId)) throw new CustomException(ErrorCode.MOVIE_NOT_FOUND);

        try {
            validateCursor(lastBoardId);
            Slice<BoardProjection> boards = boardRepository.getRecentMovieRelatedBoards(userId, movieId, lastBoardId, pageable);
            return boards;
        } catch(Exception e) {
            throw new CustomException(ErrorCode.BOARD_MOVIE_RELATED_GET_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public Slice<BoardProjection> getRecentMovieBoards(Long userId, Long lastBoardId, Pageable pageable) {
        // 특정 영화 무비로그들 최신순 기준으로 Board들을 가져온다

        try {
            validateCursor(lastBoardId);
            Slice<BoardProjection> boards = boardRepository.getRecentBoards(userId, lastBoardId, pageable);
            return boards;
        } catch(Exception e) {
            throw new CustomException(ErrorCode.BOARD_MOVIE_RELATED_GET_FAILED);
        }
    }


    @Transactional(readOnly = true)
    public Slice<BoardProjection> findBoardsByUserId(Long userId,  BoardQueryReq req, PageRequest pageRequest) {
        String nickname = req.nickname();
        Long lastBoardId = req.lastBoardId();

        if (!userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 커서 유효성 검사
        validateCursor(lastBoardId);
        try {
            return boardRepository.findByIdAndCursor(userId, lastBoardId, pageRequest);
        } catch(Exception e) {
            throw new CustomException(ErrorCode.BOARD_USER_ID_GET_FAILED);
        }
    }

    private void validateCursor(Long lastId) {
        // 첫 요청일 경우
        if(lastId == null) return;
        if(lastId <= 0) {
            throw new CustomException(ErrorCode.LAST_ID_INVALID_CURSOR);
        }
    }


    @Transactional(readOnly = true)
    public List<BoardContentWithBoardId> getBoardContentWithBoardId(List<Long> boardIds){
        return boardContentRepository.findByBoardIds(boardIds);
    }

    @Transactional(readOnly = true)
    public Slice<BoardCommentProjection> getTenBoardCommentsPerReq(Long boardId, Pageable pageable) {
        // 특정 Board 최신순 기준으로 댓글들을 가져온다
        if(!boardRepository.existsById(boardId)) throw new CustomException(ErrorCode.BOARD_NOT_FOUND);
        if(boardRepository.findIsDeleted(boardId) == IsDeleted.TRUE) throw new CustomException(ErrorCode.BOARD_IS_DELETED);

        try {
            Slice<BoardCommentProjection> comments = boardRepository.getBoardComments(boardId, pageable);
            return comments;
        } catch(Exception e) {
            throw new CustomException(ErrorCode.BOARD_COMMENT_READ_FAILED);
        }
    }

    public void checkBoardWriteUser(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        if(!board.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.BOARD_USER_NOT_WRITER);
        }
    }
    @Transactional(readOnly = true)
    public void checkBoardCommentWriteUser(Long commentId, Long userId) {
        BoardComment comment = boardCommentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_COMMENT_NOT_FOUND));

        if(!comment.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.BOARD_COMMENT_USER_NOT_WRITER);
        }
    }

    @Transactional
    public void addBoardComment(String context, Long boardId, Long userId, String userNickname) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        if(board.getIsDeleted() == IsDeleted.TRUE) throw new CustomException(ErrorCode.BOARD_IS_DELETED);
        try {
            BoardComment comment = BoardComment.of(board, userId, context, userNickname);
            boardCommentRepository.save(comment);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BOARD_COMMENT_CREATE_FAILED);
        }
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        if(board.getIsDeleted() == IsDeleted.TRUE) throw new CustomException(ErrorCode.BOARD_IS_DELETED);
        try {
            board.deleteBoard(IsDeleted.fromString("TRUE"));
            boardRepository.save(board);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BOARD_DELETE_FAILED);
        }
    }


    @Transactional
    public void deleteBoardComment(Long commentId) {
        BoardComment comment = boardCommentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_COMMENT_NOT_FOUND));
        try{
            boardCommentRepository.deleteByBoardCommentId(commentId);
        }catch (Exception e) {
            throw new CustomException(ErrorCode.BOARD_COMMENT_DELETE_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public void checkBoardIsDeleted(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        if(board.getIsDeleted() == IsDeleted.TRUE) throw new CustomException(ErrorCode.BOARD_IS_DELETED);
    }


    @Transactional(readOnly = true)
    public boolean checkUserBoardLike(Long userId, Long boardId) {
        try {
            Optional<BoardLike> board = boardLikeRepository.findByBoardIdAndUserId(boardId, userId);
            return board.isPresent();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BOARD_LIKE_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public Board getBoardById(Long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);
        return board.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    @Transactional
    public void deleteBoardLike(Long userId, Long boardId) {
        try {
            boardLikeRepository.deleteByBoardIdAndUserId(boardId, userId);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BOARD_LIKE_DELETE_FAILED);
        }
    }

    @Transactional
    public void createBoardLike(Long userId, Board board) {
        try{
            BoardLike like = BoardLike.of(userId, board);
            boardLikeRepository.save(like);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BOARD_LIKE_DELETE_FAILED);
        }
    }



}
