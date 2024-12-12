package com.ureca.picky_be.base.business.lineReview;

import com.ureca.picky_be.base.business.lineReview.dto.*;
import com.ureca.picky_be.base.business.user.dto.UserLineReviewsReq;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

public interface LineReviewUseCase {

    CreateLineReviewResp createLineReview(CreateLineReviewReq createLineReviewReq);

    UpdateLineReviewResp updateLineReview(Long lineReviewId, UpdateLineReviewReq req);

    Slice<ReadLineReviewResp> getLineReviewsByMovie(PageRequest pageRequest, LineReviewQueryRequest queryReq);
    Slice<ReadLineReviewResp> getLineReviewsByNickname(PageRequest pageRequest, UserLineReviewsReq req);
}
