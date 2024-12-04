package com.ureca.picky_be.base.presentation.controller.board;


import com.ureca.picky_be.base.business.board.BoardUseCase;
import com.ureca.picky_be.base.business.board.dto.AddBoardCommentReq;
import com.ureca.picky_be.base.business.board.dto.AddBoardReq;
import com.ureca.picky_be.base.business.board.dto.GetBoardInfoResp;
import com.ureca.picky_be.base.business.board.dto.UpdateBoardReq;
import com.ureca.picky_be.global.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

    private final BoardUseCase boardUseCase;

    @PostMapping("")
    @Operation(summary = "게시글 생성(Create)", description = "사용자가 입력한 게시글 생성, contentType으로 'PHOTO', 'VIDEO'로 입력해야합니다. 또한, 현재 글만 입력 가능합니다!")
    public SuccessCode createBoard(@RequestBody AddBoardReq req) {
        boardUseCase.addBoard(req);
        return SuccessCode.CREATE_BOARD_SUCCESS;
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
    public Slice<GetBoardInfoResp> getBoardInfo(
            @PathVariable Long movieId,
            @Parameter(description = "0 < size <= 10") @RequestParam(defaultValue = "10") int size,
            @RequestParam int page) {

        // TODO : Slice 형태로 return 했을 때, 차후 프론트에서 문제 발생 시 List로 return 해줘야함.
        return boardUseCase.getMovieRelatedBoards(movieId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }





//    @GetMapping("/{boardId}/comments")
//    @Operation(summary = "게시글 댓글 조회(Read)", description = "사용자가 게시들 댓글들 조회")
//    public ResponseEntity<ApiResponse<GetAllBoardComments>> getBoardComments(@PathVariable Long boardId) {
//
//
//    }


}
