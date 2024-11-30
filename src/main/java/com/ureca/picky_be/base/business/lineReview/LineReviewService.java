package com.ureca.picky_be.base.business.lineReview;

import com.ureca.picky_be.base.business.lineReview.dto.CreateLineReviewReq;
import com.ureca.picky_be.base.business.lineReview.dto.CreateLineReviewResp;
import com.ureca.picky_be.base.business.lineReview.dto.UpdateLineReviewReq;
import com.ureca.picky_be.base.business.lineReview.dto.UpdateLineReviewResp;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.lineReview.LineReviewManager;
import com.ureca.picky_be.base.implementation.lineReview.mapper.LineReviewMapper;
import com.ureca.picky_be.jpa.lineReview.LineReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class LineReviewService implements LineReviewUseCase {

    private final LineReviewManager lineReviewManager;
    private final LineReviewMapper lineReviewMapper;
    private final AuthManager authManager;

    @Override
    @Transactional
    public CreateLineReviewResp createLineReview(CreateLineReviewReq req) {
        Long userId = authManager.getUserId();
        LineReview newLineReview = lineReviewManager.createLineReview(req, userId);
        return lineReviewMapper.createLineReviewResp(newLineReview);
    }

    @Override
    @Transactional
    public UpdateLineReviewResp updateLineReview(Long lineReviewId, UpdateLineReviewReq req) {
        Long userId = authManager.getUserId();
        LineReview updateLineReview = lineReviewManager.updateLineReview(lineReviewId,req,userId);
        return lineReviewMapper.updateLineReview(updateLineReview);
    }


}
