package com.ureca.picky_be.base.business.board;

import com.ureca.picky_be.base.business.board.dto.*;
import com.ureca.picky_be.base.implementation.board.BoardManager;
import com.ureca.picky_be.base.implementation.movie.MovieManager;
import com.ureca.picky_be.jpa.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardManagingService implements BoardManagingUseCase {

    private final BoardManager boardManager;
    private final MovieManager movieManager;


    @Override
    public AddBoardResp addBoard(AddBoardReq req, Long userId) {
        // 차후 영상, 사진을 S3에 넣고 url로 받아야하는 파트 추후 구현

        /**
         * 흐름도
         * # content 유무 검사 필요할듯
         * 1. Board entity, BoardContent Entity에 따로따로 생성
         * 2. Builder에 값들 넣고(나중에는 컨텐츠 S3 업로드 그런 과정 필요)
         * 3. save
         *
         */

        Board board = boardManager.createBoardWithContents(userId, req);

        return null;
    }

    @Override
    public GetMovieLogBoardsResp getMovieLogBoards() {
        /**
         * 1. 최신순으로 MovieLog들 조회해서 가져오기
         *  
         *
         */
        return null;
    }

    @Override
    public GetMovieRelatedBoardsResp getMovieRelatedBoards(Long movieId) {
        /**
         * 1. movieId 기반으로 최신 무비로그들 GET
         */

        return null;
    }

    @Override
    public UpdateBoardResp updateBoard(UpdateBoardReq req, Long userId, Long boardId) {
        /**
         * 1. boardId가 userId가 작성한 건지 확인 검사
         * 2. 해당 board로 req로 수정
         * 3. 저장
         */
        return null;
    }

    @Override
    public DeleteBoardResp deleteBoard(Long userId, Long boardId) {
        /**
         * 1. boardId로 해당 보드가 user가 작성한 건지 검증 로직
         * 2. 해당 Board 삭제(Board에 작성된 댓글, 좋아요 전부 삭제한다)
         */
        return null;
    }

    @Override
    public AddOrDeleteBoardLikeResp addBoardLike(Long userId, Long boardId) {
        /**
         * 1. board에 user가 좋아요 누른 여부 파악
         * 2. 눌렀으면 삭제, 안눌렀으면 좋아요 생성
         * 3. 결과에 따라 response
         */
        return null;
    }

    @Override
    public AddBoardCommentResp addBoardComment(AddBoardCommentReq req, Long userId, Long boardId) {
        /**
         * 1. boardId에 user 댓글 작성
         */
        return null;
    }

    @Override
    public DeleteBoardCommentsResp deleteBoardComment(Long userId, Long boardId, Long commentId) {
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
