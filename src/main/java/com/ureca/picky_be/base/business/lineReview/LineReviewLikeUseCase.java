package com.ureca.picky_be.base.business.lineReview;

import com.ureca.picky_be.base.business.lineReview.dto.*;
import com.ureca.picky_be.global.success.SuccessCode;

public interface LineReviewLikeUseCase {

    SuccessCode createLineReviewLike(CreateLineReviewLikeReq createLineReviewLikeReq);

    CountLineReviewLikeResp countLineReviewLike(Long lineReviewId);
}
