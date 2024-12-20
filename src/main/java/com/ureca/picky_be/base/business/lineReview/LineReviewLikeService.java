package com.ureca.picky_be.base.business.lineReview;

import com.ureca.picky_be.base.business.lineReview.dto.*;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.lineReview.LineReviewLikeManager;
import com.ureca.picky_be.base.implementation.mapper.LineReviewLikeDtoMapper;
import com.ureca.picky_be.global.success.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class LineReviewLikeService implements LineReviewLikeUseCase {

    private final LineReviewLikeManager lineReviewLikeManager;
    private final LineReviewLikeDtoMapper lineReviewLikeDtoMapper;
    private final AuthManager authManager;


    @Override
    @Transactional
    public SuccessCode createLineReviewLike(CreateLineReviewLikeReq req) {
        Long userId = authManager.getUserId();
        return lineReviewLikeManager.createLineReviewLike(req,userId);
    }

    @Override
    public CountLineReviewLikeResp countLineReviewLike(Long lineReviewId) {
        return lineReviewLikeManager.countLineReviewLike(lineReviewId);
    }
}
