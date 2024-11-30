package com.ureca.picky_be.base.business.lineReview;

import com.ureca.picky_be.base.business.lineReview.dto.CreateLineReviewReq;
import com.ureca.picky_be.base.business.lineReview.dto.CreateLineReviewResp;
import com.ureca.picky_be.base.business.lineReview.dto.UpdateLineReviewReq;
import com.ureca.picky_be.base.business.lineReview.dto.UpdateLineReviewResp;

public interface LineReviewUseCase {

    CreateLineReviewResp createLineReview(CreateLineReviewReq createLineReviewReq);

    UpdateLineReviewResp updateLineReview(Long lineReviewId, UpdateLineReviewReq req);
}
