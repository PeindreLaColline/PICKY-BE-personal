package com.ureca.picky_be.base.business.lineReview;

import com.ureca.picky_be.base.business.lineReview.dto.*;
import com.ureca.picky_be.base.business.user.dto.UserLineReviewsReq;
import com.ureca.picky_be.global.success.SuccessCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

public interface LineReviewUseCase {

    CreateLineReviewResp createLineReview(CreateLineReviewReq createLineReviewReq);

    UpdateLineReviewResp updateLineReview(Long lineReviewId, UpdateLineReviewReq req);

    Slice<ReadLineReviewResp> getLineReviewsByMovie(PageRequest pageRequest, LineReviewQueryRequest queryReq);
    Slice<GetUserLineReviewResp> getLineReviewsByNickname(PageRequest pageRequest, UserLineReviewsReq req);

    GetLineReviewRatingInfoResp getRatingLineReviewInfo(Long movieId);

    GetLineReviewGenderInfoResp getGenderLineReviewInfo(Long movieId);

    SuccessCode deleteLineReview(Long lineReviewId);
}
