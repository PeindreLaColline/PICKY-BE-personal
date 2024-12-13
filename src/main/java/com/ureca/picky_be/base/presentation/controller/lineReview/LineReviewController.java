package com.ureca.picky_be.base.presentation.controller.lineReview;


import com.ureca.picky_be.base.business.lineReview.LineReviewUseCase;
import com.ureca.picky_be.base.business.lineReview.dto.*;
import com.ureca.picky_be.base.business.user.dto.UserLineReviewsReq;
import com.ureca.picky_be.jpa.lineReview.SortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

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
    @PatchMapping("/{lineReviewId}")
    public UpdateLineReviewResp updateLineReview(@PathVariable Long lineReviewId, @RequestBody UpdateLineReviewReq req){
        UpdateLineReviewResp resp = lineReviewUseCase.updateLineReview(lineReviewId, req);
        return resp;
    }

    @Operation(
            summary = "한줄평 조회",
            description = """
        - size: 0~10 사이의 값으로 설정 기본값: 10).
        - 정렬 방식:
          - LATEST: 최신순으로 정렬.  `lastReviewId`와 `lastCreatedAt`이 모두 필요.
          - LIKES: 좋아요가 많은 순으로 정렬. `lastReviewId`만 필요하며, `lastCreatedAt`은 사용하지 않음.
        - 처음 요청에는 `lastReviewId`와 `lastCreatedAt`이 필요하지 않으며, null로 설정.
        - 다음 페이지 요청 시, 마지막 리뷰 정보를 기반으로 `lastReviewId`를 반드시 포함해야 하며, LATEST 정렬의 경우 `lastCreatedAt`도 포함해야 함.
        """
    )

    @GetMapping("/movie/{movieId}")
    public Slice<ReadLineReviewResp> readLineReview(
            @PathVariable Long movieId,
            @Parameter(description = "0 < size <= 10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = """
            정렬 방식:
            - LATEST: 최신순 정렬 (lastReviewId와 lastCreatedAt 필요)
            - LIKES: 좋아요 많은 순 정렬 (lastReviewId만 필요)
        """) @RequestParam(defaultValue = "LATEST") SortType sortType,
            @RequestParam(required = false) Long lastReviewId,
            @RequestParam(required = false) LocalDateTime lastCreatedAt) {

        LineReviewQueryRequest queryReq = new LineReviewQueryRequest(movieId, lastReviewId, lastCreatedAt, sortType);

        return lineReviewUseCase.getLineReviewsByMovie(PageRequest.ofSize(size), queryReq);
    }

    @GetMapping("user/{nickname}")
    @Operation(summary = "닉네임으로 해당 사용자가 작성한 한줄평 조회", description = "마이페이지에서 사용자 닉네임으로 해당 사용자가 작성한 한줄평들을 확인하는 API입니다.")
    public Slice<GetUserLineReviewResp> getUserLineReviews(
            @PathVariable String nickname,
            @Parameter(description = "0 < size <= 10") @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long lastReviewId) {

        UserLineReviewsReq req = new UserLineReviewsReq(nickname, lastReviewId);

        return lineReviewUseCase.getLineReviewsByNickname(PageRequest.ofSize(size), req);
    }

    @GetMapping("/movie/{movieId}/genders")
    @Operation(summary = "특정 영화에 대한 한줄평 작성자 성별에 따른 데이터 조회 API", description = "해당 영화의 전체 작성자, 남성, 여성 작성자수, 성별별 평점 평균 데이터가 있습니다.")
    public GetLineReviewGenderInfoResp getGenderLineReviewInfo(@PathVariable Long movieId) {
        return lineReviewUseCase.getGenderLineReviewInfo(movieId);
    }

    @GetMapping("/movie/{movieId}/ratings")
    @Operation(summary = "특정 영화에 대한 한줄평 평점별 비율 데이터 조회 API", description = "해당 영화 한줄평 평점들(1~5까지) 각 점수별 사용자 수를 출력하는 API입니다.")
    public GetLineReviewRatingInfoResp getRatingPercentage(@PathVariable Long movieId) {
        return lineReviewUseCase.getRatingLineReviewInfo(movieId);
    }
}
