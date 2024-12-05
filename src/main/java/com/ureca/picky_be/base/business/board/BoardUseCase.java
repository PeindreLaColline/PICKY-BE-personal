package com.ureca.picky_be.base.business.board;

import com.ureca.picky_be.base.business.board.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface BoardUseCase {
    void addBoard(AddBoardReq req);   // 게시글 생성
    void updateBoard(Long boardId, UpdateBoardReq req);                          // 본인이 작성한 게시글 수정
    DeleteBoardResp deleteBoard(Long boardId);                          // 본인이 작성한 게시글 삭제

    Slice<GetBoardInfoResp> getBoards(Pageable pageable);   // 무비로그 메인 페이지 게시글들 가져오기(최신순)
    Slice<GetBoardInfoResp> getMovieRelatedBoards(Long movieId, Pageable pageable);     // 특정 영화들에 대한 무비 로그 가져오기

    AddOrDeleteBoardLikeResp addBoardLike(Long boardId);                        // 게시글 좋아요
    void addBoardComment(AddBoardCommentReq req, Long boardId);                  // 댓글 작성
    DeleteBoardCommentsResp deleteBoardComment(Long boardId, Long commentId);           // 댓글 삭제
    GetAllBoardComments getAllBoardComments(Long boardId);              // 게시물에 대한 모든 댓글 읽기

}
