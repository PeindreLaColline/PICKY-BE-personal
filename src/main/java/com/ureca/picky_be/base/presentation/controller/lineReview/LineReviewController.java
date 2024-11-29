package com.ureca.picky_be.base.presentation.controller.lineReview;


import com.ureca.picky_be.base.business.lineReview.LineReviewUseCase;
import com.ureca.picky_be.base.business.lineReview.dto.CreateLineReviewReq;
import com.ureca.picky_be.base.business.lineReview.dto.CreateLineReviewResp;
import com.ureca.picky_be.base.business.lineReview.dto.UpdateLineReviewReq;
import com.ureca.picky_be.base.business.lineReview.dto.UpdateLineReviewResp;
import com.ureca.picky_be.jpa.lineReview.LineReview;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/linereview")
public class LineReviewController {

    private final LineReviewUseCase lineReviewUseCase;

    @PostMapping("/create")
    public CreateLineReviewResp createLineReview(@RequestBody CreateLineReviewReq req) {
        CreateLineReviewResp resp = lineReviewUseCase.createLineReview(req);
        return resp;
    }

    @PutMapping("/{lineReviewId}")
    public UpdateLineReviewResp updateLineReviewResp(@PathVariable Long lineReviewId, @RequestBody UpdateLineReviewReq req){
        UpdateLineReviewResp resp = lineReviewUseCase.updateLineReview(lineReviewId, req);
        return resp;
    }






}
