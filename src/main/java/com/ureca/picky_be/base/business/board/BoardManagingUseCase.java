package com.ureca.picky_be.base.business.board;

import com.ureca.picky_be.base.business.board.dto.*;

public interface BoardManagingUseCase {
    AddBoardResp addBoard(AddBoardReq req, Long userId);   // 게시글 생성
    GetMovieLogBoardsResp getMovieLogBoards();   // 무비로그 메인 페이지 게시글들 가져오기(최신순)
    GetMovieRelatedBoardsResp getMovieRelatedBoards(Long movieId);     // 특정 영화들에 대한 무비 로그 가져오기
    UpdateBoardResp updateBoard(UpdateBoardReq req, Long userId, Long boardId);                          // 본인이 작성한 게시글 수정
    DeleteBoardResp deleteBoard(Long userId, Long boardId);                          // 본인이 작성한 게시글 삭제
    AddOrDeleteBoardLikeResp addBoardLike(Long userId, Long boardId);                        // 게시글 좋아요
    AddBoardCommentResp addBoardComment(AddBoardCommentReq req, Long userId, Long boardId);                  // 댓글 작성
    DeleteBoardCommentsResp deleteBoardComment(Long userId, Long boardId, Long commentId);           // 댓글 삭제
    GetAllBoardComments getAllBoardComments(Long boardId);              // 게시물에 대한 모든 댓글 읽기

}
