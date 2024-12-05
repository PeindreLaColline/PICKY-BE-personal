package com.ureca.picky_be.base.business.board;

import com.ureca.picky_be.base.business.board.dto.*;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.board.BoardManager;
import com.ureca.picky_be.base.implementation.mapper.BoardDtoMapper;
import com.ureca.picky_be.base.persistence.board.BoardRepository;
import com.ureca.picky_be.jpa.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService implements BoardUseCase {

    private final BoardManager boardManager;
    private final AuthManager authManager;
    private final BoardRepository boardRepository;
    private final BoardDtoMapper boardDtoMapper;

    @Override
    @Transactional
    public void addBoard(AddBoardReq req) {
        // TODO: boardContent S3 업로드 등 처리 추가 예정
        Long userId = authManager.getUserId();
        String userNickname = authManager.getUserNickname();
        boardManager.addBoard(userId, userNickname, req);
    }

    @Override
    @Transactional
    public void updateBoard(Long boardId , UpdateBoardReq req) {
        Long userId = authManager.getUserId();
        boardManager.checkBoardWriteUser(boardId, userId);
        boardManager.updateBoard(boardId, userId, req);
    }

    @Override
    public Slice<GetBoardInfoResp> getBoards(Pageable pageable) {

        /**
         * 무비로그 탭을 누르면 그냥 최신 Board들 가져오는 것
         * 1. movieId 존재하는지 검사
         * 2. board들 가져오기
         * 3.
         */

        Long userId = authManager.getUserId();
        Slice<BoardProjection> recentBoards = boardManager.getRecentMovieBoards(userId, pageable);
        return recentBoards.map(boardDtoMapper::toGetBoardInfoResp);
    }

    @Override
    public Slice<GetBoardInfoResp> getMovieRelatedBoards(Long movieId, Pageable pageable) {
        /**
         * 특정 영화 상세페이지에서 무비 로그 누르면 나오는 데이터들(특정 영화에 대한 무비 로그들 최신순 기반으로 가져오기)
         * 1. movieId 기반으로 최신 무비로그들 GET
         */

        Long userId = authManager.getUserId();
        Slice<BoardProjection> recentMovieRelatedBoards = boardManager.getRecentMovieRelatedBoards(userId, movieId, pageable);
        return recentMovieRelatedBoards.map(boardDtoMapper::toGetBoardInfoResp);
    }



    @Override
    public DeleteBoardResp deleteBoard(Long boardId) {
        /**
         * 1. boardId로 해당 보드가 user가 작성한 건지 검증 로직
         * 2. 해당 Board 삭제(Board에 작성된 댓글, 좋아요 전부 삭제한다)
         */
        return null;
    }

    @Override
    public AddOrDeleteBoardLikeResp addBoardLike(Long boardId) {
        /**
         * 1. board에 user가 좋아요 누른 여부 파악
         * 2. 눌렀으면 삭제, 안눌렀으면 좋아요 생성
         * 3. 결과에 따라 response
         */

        return null;
    }

    @Override
    public void addBoardComment(AddBoardCommentReq req, Long boardId) {
        /**
         * 1. boardId에 user 댓글 작성
         * 2.
         */
        Long userId = authManager.getUserId();
        String userNickname = authManager.getUserNickname();
        boardManager.addBoardComment(req.content(), boardId, userId, userNickname);
    }

    @Override
    public DeleteBoardCommentsResp deleteBoardComment(Long boardId, Long commentId) {
        /**
         * 1. 삭제하려는 comment가 user것인지 검사
         * 2. 일치하면 삭제
         */
        return null;
    }

    @Override
    public GetAllBoardComments getAllBoardComments(Long boardId) {
        /**
         * 1. boardId에 있는 댓글들 싹 가져오기?
         */
        return null;
    }
}
