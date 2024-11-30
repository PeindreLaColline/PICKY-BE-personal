package com.ureca.picky_be.base.presentation.controller.lineReview;


import com.ureca.picky_be.base.business.lineReview.LineReviewUseCase;
import com.ureca.picky_be.base.business.lineReview.dto.CreateLineReviewReq;
import com.ureca.picky_be.base.business.lineReview.dto.CreateLineReviewResp;
import com.ureca.picky_be.base.business.lineReview.dto.UpdateLineReviewReq;
import com.ureca.picky_be.base.business.lineReview.dto.UpdateLineReviewResp;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/linereview")
public class LineReviewController {

    private final LineReviewUseCase lineReviewUseCase;


    @Operation(summary = "한줄평 생성", description = "영화당 한줄평 한개만 가능, 평점은 0에서 5점")
    @PostMapping("/create")
    public CreateLineReviewResp createLineReview(@RequestBody CreateLineReviewReq req) {
        CreateLineReviewResp resp = lineReviewUseCase.createLineReview(req);
        return resp;
    }
    @Operation(summary = "한줄평 없데이트", description = "평점은 수정 불가능")
    @PutMapping("/{lineReviewId}")
    public UpdateLineReviewResp updateLineReviewResp(@PathVariable Long lineReviewId, @RequestBody UpdateLineReviewReq req){
        UpdateLineReviewResp resp = lineReviewUseCase.updateLineReview(lineReviewId, req);
        return resp;
    }






}
