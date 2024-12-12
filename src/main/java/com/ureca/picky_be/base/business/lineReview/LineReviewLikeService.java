package com.ureca.picky_be.base.business.lineReview;

import com.ureca.picky_be.base.business.lineReview.dto.*;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.lineReview.LineReviewLikeManager;
import com.ureca.picky_be.base.implementation.lineReview.mapper.LineReviewLikeMapper;
import com.ureca.picky_be.jpa.lineReview.LineReviewLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class LineReviewLikeService implements LineReviewLikeUseCase {

    private final LineReviewLikeManager lineReviewLikeManager;
    private final LineReviewLikeMapper lineReviewLikeMapper;
    private final AuthManager authManager;


    @Override
    @Transactional
    public CreateLineReviewLikeResp createLineReviewLike(CreateLineReviewLikeReq req) {
        Long userId = authManager.getUserId();
        LineReviewLike newLineReviewLike = lineReviewLikeManager.createLineReviewLike(req,userId);
        return lineReviewLikeMapper.createLineReviewLikeResp(newLineReviewLike);
    }

    @Override
    public CountLineReviewLikeResp countLineReviewLike(Long lineReviewId) {
        return lineReviewLikeManager.countLineReviewLike(lineReviewId);
    }
}
