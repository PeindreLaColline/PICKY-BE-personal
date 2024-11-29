package com.ureca.picky_be.base.business.lineReview;

import com.ureca.picky_be.base.business.lineReview.dto.*;

public interface LineReviewLikeUseCase {

    CreateLineReviewLikeResp createLineReviewLike(CreateLineReviewLikeReq createLineReviewLikeReq);

    CountLineReviewLikeResp countLineReviewLike(Long lineReviewId);
}
