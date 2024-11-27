package com.ureca.picky_be.base.business.board;

import com.ureca.picky_be.base.business.board.dto.*;

public interface BoardManagingUseCase {
    AddBoardResp addBoard(AddBoardReq req);   // 게시글 생성
    GetMovieLogBoardsResp getMovieLogBoards();   // 무비로그 메인 페이지 게시글들 가져오기(최신순)
    GetMovieRelatedBoardsResp getMovieRelatedBoards();     // 특정 영화들에 대한 무비 로그 가져오기
    UpdateBoardResp updateBoard(UpdateBoardReq req);                          // 본인이 작성한 게시글 수정
    DeleteBoardResp deleteBoard();                          // 본인이 작성한 게시글 삭제
    AddBoardLikeResp addBoardLike();                        // 게시글 좋아요
    DeleteBoardLikeResp deleteBoardLike();                  // 게시글 좋아요 취소
    AddBoardCommentResp addBoardComment(AddBoardCommentReq req);                  // 댓글 작성
    DeleteBoardCommentsResp deleteBoardComment();           // 댓글 삭제
    GetAllBoardComments getAllBoardComments();              // 게시물에 대한 모든 댓글 읽기

}
