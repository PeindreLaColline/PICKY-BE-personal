package com.ureca.picky_be.base.business.lineReview;

import com.ureca.picky_be.base.business.lineReview.dto.*;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.lineReview.LineReviewManager;
import com.ureca.picky_be.base.implementation.lineReview.mapper.LineReviewDtoMapper;
import com.ureca.picky_be.jpa.lineReview.LineReview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class LineReviewService implements LineReviewUseCase {

    private final LineReviewManager lineReviewManager;
    private final LineReviewDtoMapper lineReviewDtoMapper;
    private final AuthManager authManager;

    @Override
    @Transactional
    public CreateLineReviewResp createLineReview(CreateLineReviewReq req) {
        Long userId = authManager.getUserId();
        String userNickname = authManager.getUserNickname();
        LineReview newLineReview = lineReviewManager.createLineReview(req, userId, userNickname);
        return lineReviewDtoMapper.createLineReviewResp(newLineReview);
    }

    @Override
    @Transactional
    public UpdateLineReviewResp updateLineReview(Long lineReviewId, UpdateLineReviewReq req) {
        Long userId = authManager.getUserId();
        LineReview updateLineReview = lineReviewManager.updateLineReview(lineReviewId,req,userId);
        return lineReviewDtoMapper.updateLineReview(updateLineReview);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<ReadLineReviewResp> getLineReviewsByMovie(PageRequest pageRequest, LineReviewQueryRequest queryReq) {
        Slice<LineReviewProjection> lineReviews = lineReviewManager.findLineReviewsByMovie(queryReq, pageRequest);
        return lineReviews.map(lineReviewDtoMapper::toReadLineReviewResp);
    }


}
