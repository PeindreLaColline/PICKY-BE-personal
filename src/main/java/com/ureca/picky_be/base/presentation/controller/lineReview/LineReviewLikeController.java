package com.ureca.picky_be.base.presentation.controller.lineReview;


import com.ureca.picky_be.base.business.lineReview.LineReviewLikeUseCase;
import com.ureca.picky_be.base.business.lineReview.LineReviewUseCase;
import com.ureca.picky_be.base.business.lineReview.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/linereviewlike")
public class LineReviewLikeController {

    private final LineReviewLikeUseCase lineReviewlikeUseCase;

    @PostMapping("/")
    public CreateLineReviewLikeResp createLineReviewLike(@RequestBody CreateLineReviewLikeReq req) {
        CreateLineReviewLikeResp resp = lineReviewlikeUseCase.createLineReviewLike(req);
        return resp;
    }

    @GetMapping("/{lineReviewId}")
    public CountLineReviewLikeResp countLineReviewLike(@PathVariable Long lineReviewId){
        CountLineReviewLikeResp resp = lineReviewlikeUseCase.countLineReviewLike(lineReviewId);
        return resp;
    }


}
