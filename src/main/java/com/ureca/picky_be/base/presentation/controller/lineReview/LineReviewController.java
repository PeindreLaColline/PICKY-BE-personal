package com.ureca.picky_be.base.presentation.controller.lineReview;


import com.ureca.picky_be.base.business.lineReview.LineReviewUseCase;
import com.ureca.picky_be.base.business.lineReview.dto.*;
import com.ureca.picky_be.jpa.lineReview.LineReview;
import com.ureca.picky_be.jpa.lineReview.SortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @Operation(summary = "한줄평 업데이트", description = "평점은 수정 불가능")
    @PutMapping("/{lineReviewId}")
    public UpdateLineReviewResp updateLineReviewResp(@PathVariable Long lineReviewId, @RequestBody UpdateLineReviewReq req){
        UpdateLineReviewResp resp = lineReviewUseCase.updateLineReview(lineReviewId, req);
        return resp;
    }

    @GetMapping("/movie/{movieId}")
    public Slice<ReadLineReviewResp> readLineReviewResp(
            @PathVariable Long movieId,
            @Parameter(description = "0 < size <= 10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 방식: LATEST(최신순), LIKES(좋아요 많은 순)") @RequestParam(defaultValue = "LATEST") SortType sortType ,
            @RequestParam(required = false)  Long lastReviewId,
            // 클라이언트가 처음 데이터를 요청할 때는 lastReviewId와 lastCreatedAt이 필요 없음 그래서 required = false
            @RequestParam(required = false) LocalDateTime lastCreatedAt){
        LineReviewQueryRequest queryReq = new LineReviewQueryRequest(movieId, lastReviewId, lastCreatedAt, sortType);
        return lineReviewUseCase.getLineReviewsByMovie(PageRequest.ofSize(size), queryReq);
    }





}
