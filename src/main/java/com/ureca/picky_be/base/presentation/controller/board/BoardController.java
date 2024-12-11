package com.ureca.picky_be.base.presentation.controller.board;


import com.ureca.picky_be.base.business.board.BoardUseCase;
import com.ureca.picky_be.base.business.board.dto.commentDto.AddBoardCommentReq;
import com.ureca.picky_be.base.business.board.dto.boardDto.AddBoardReq;
import com.ureca.picky_be.base.business.board.dto.boardDto.GetBoardInfoResp;
import com.ureca.picky_be.base.business.board.dto.boardDto.UpdateBoardReq;
import com.ureca.picky_be.base.business.board.dto.commentDto.GetAllBoardCommentsResp;
import com.ureca.picky_be.global.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

    private final BoardUseCase boardUseCase;

    @PostMapping
    @Operation(summary = "사진 및 영상을 포함한 무비로그 생성(Create)", description = "헤더 설정 -> content-type : multipart/form-data 이렇게 해서 아래 요구되는대로 보내주시면 됩니다")
    public SuccessCode createBoard(@RequestPart(value="request") AddBoardReq req,
                                   @RequestPart(value="image", required = false) List<MultipartFile> images,
                                   @RequestPart(value="video", required = false) List<MultipartFile> videos) throws IOException {
        return boardUseCase.addBoard(req, images, videos);
    }

    @PostMapping("/{boardId}")
    @Operation(summary = "게시글 수정(Update)", description = "사용자가 작성한 게시글 수정(글 내용(Context), 스포일러 여부(isSpoiler)만 수정 가능합니다!")
    public SuccessCode updateBoard(@PathVariable Long boardId, @RequestBody UpdateBoardReq req) {
        boardUseCase.updateBoard(boardId, req);
        return SuccessCode.UPDATE_BOARD_SUCCESS;
    }

    @PostMapping("/{boardId}/comment")
    @Operation(summary = "게시글 댓글 생성(Create)", description = "사용자가 게시들에 댓글 작성")
    public SuccessCode addBoardComment(@PathVariable Long boardId, @RequestBody AddBoardCommentReq req) {
        boardUseCase.addBoardComment(req, boardId);
        return SuccessCode.CREATE_BOARD_COMMENT_SUCCESS;
    }

    @GetMapping("/{movieId}")
    @Operation(summary = "영화 상세보기 -> 무비로그용 API", description = "특정 영화에 대한 무비 로그들을 최신순 기반으로 가져오는 API입니다.")
    public Slice<GetBoardInfoResp> getMovieBoardsInfo(
            @PathVariable Long movieId,
            @Parameter(description = "0 < size <= 10") @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return boardUseCase.getMovieRelatedBoards(movieId, pageable);
    }

    @GetMapping("/all")
    @Operation(summary = "최신 무비로그용 API", description = "무비 로그들을 최신순 기반으로 가져오는 API입니다.")
    public Slice<GetBoardInfoResp> getBoardsInfo(
            @Parameter(description = "0 < size <= 10") @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return boardUseCase.getBoards(pageable);
    }

    @GetMapping("/{boardId}/comments")
    @Operation(summary = "댓글 조회용 API", description = "특정 무비 로그에 대한 댓글들을 조회하는 API입니다.")
    public Slice<GetAllBoardCommentsResp> getBoardsComments(
            @PathVariable Long boardId,
            @Parameter(description = "0 < size <= 10") @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return boardUseCase.getAllBoardComments(boardId, pageable);
    }


    @DeleteMapping("/{boardId}")
    @Operation(summary = "게시글 삭제", description = "작성자가 게시글 삭제하는 경우")
    public SuccessCode deleteBoard(@PathVariable Long boardId) {
        boardUseCase.deleteBoard(boardId);
        return SuccessCode.DELETE_BOARD_SUCCESS;
    }

    @DeleteMapping("/{boardId}/comments")
    @Operation(summary = "댓글 삭제", description = "작성자가 댓글 삭제하는 경우")
    public SuccessCode deleteBoardComment(@PathVariable Long boardId, @RequestParam Long commentId) {
        boardUseCase.deleteBoardComment(boardId, commentId);
        return SuccessCode.DELETE_BOARD_COMMENT_SUCCESS;
    }


    @PostMapping("/{boardId}/likes")
    @Operation(summary = "게시글 좋아요", description = "게시글에 좋아요 누르기. 이미 눌러져 있다면 취소된다.")
    public SuccessCode createBoardLike(@PathVariable Long boardId) {
        if(boardUseCase.createBoardLike(boardId)) return SuccessCode.CREATE_BOARD_LIKE_SUCCESS;
        return SuccessCode.DELETE_BOARD_LIKE_SUCCESS;

    }

}
