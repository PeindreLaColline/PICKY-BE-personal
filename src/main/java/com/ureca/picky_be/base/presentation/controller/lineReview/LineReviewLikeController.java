package com.ureca.picky_be.base.presentation.controller.lineReview;


import com.ureca.picky_be.base.business.lineReview.LineReviewLikeUseCase;
import com.ureca.picky_be.base.business.lineReview.LineReviewUseCase;
import com.ureca.picky_be.base.business.lineReview.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/linereviewlike")
public class LineReviewLikeController {

    private final LineReviewLikeUseCase lineReviewlikeUseCase;

    @Operation(summary = "한줄평 좋아요/싫어요 생성", description = "중복 불가능, 자기 글 좋아요 불가능, 좋아요 누르고 싫어요 누르면 자동으로 업데이트")
    @PostMapping
    public CreateLineReviewLikeResp createLineReviewLike(@RequestBody CreateLineReviewLikeReq req) {
        CreateLineReviewLikeResp resp = lineReviewlikeUseCase.createLineReviewLike(req);
        return resp;
    }

    @Operation(summary = "한줄평 좋아요 개수", description = "그냥 한번 만들어 봤어요")
    @GetMapping("/{lineReviewId}")
    public CountLineReviewLikeResp countLineReviewLike(@PathVariable Long lineReviewId){
        CountLineReviewLikeResp resp = lineReviewlikeUseCase.countLineReviewLike(lineReviewId);
        return resp;
    }


}
