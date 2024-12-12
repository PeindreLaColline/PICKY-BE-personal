package com.ureca.picky_be.base.business.board;

import com.ureca.picky_be.base.business.board.dto.boardDto.AddBoardReq;
import com.ureca.picky_be.base.business.board.dto.boardDto.GetBoardInfoResp;
import com.ureca.picky_be.base.business.board.dto.boardDto.UpdateBoardReq;
import com.ureca.picky_be.base.business.board.dto.commentDto.AddBoardCommentReq;
import com.ureca.picky_be.base.business.board.dto.commentDto.GetAllBoardCommentsResp;
import com.ureca.picky_be.base.business.user.dto.BoardQueryReq;
import com.ureca.picky_be.global.success.SuccessCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface BoardUseCase {
    SuccessCode addBoard(AddBoardReq req, List<MultipartFile> images, List<MultipartFile> videos) throws IOException;   // 게시글 생성
    void updateBoard(Long boardId, UpdateBoardReq req);                          // 본인이 작성한 게시글 수정
    void deleteBoard(Long boardId);                          // 본인이 작성한 게시글 삭제

    Slice<GetBoardInfoResp> getBoards(Pageable pageable);   // 무비로그 메인 페이지 게시글들 가져오기(최신순)
    Slice<GetBoardInfoResp> getMovieRelatedBoards(Long movieId, Pageable pageable);     // 특정 영화들에 대한 무비 로그 가져오기

    void addBoardComment(AddBoardCommentReq req, Long boardId);                  // 댓글 작성
    void deleteBoardComment(Long boardId, Long commentId);           // 댓글 삭제
    Slice<GetAllBoardCommentsResp> getAllBoardComments(Long boardId, Pageable pageable);              // 게시물에 대한 모든 댓글 읽기

    boolean createBoardLike(Long boardId);
    Slice<GetBoardInfoResp> getBoardsByNickName(PageRequest pageRequest, BoardQueryReq req);
}
