package com.ureca.picky_be.base.business.lineReview;

import com.ureca.picky_be.base.business.lineReview.dto.*;
import com.ureca.picky_be.base.business.user.dto.UserLineReviewsReq;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.lineReview.LineReviewManager;
import com.ureca.picky_be.base.implementation.lineReview.mapper.LineReviewDtoMapper;
import com.ureca.picky_be.base.implementation.user.UserManager;
import com.ureca.picky_be.jpa.entity.lineReview.LineReview;
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
    private final UserManager userManager;

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
        Long userId = authManager.getUserId();
        Slice<LineReviewProjection> lineReviews = lineReviewManager.findLineReviewsByMovie(userId, queryReq, pageRequest);
        return lineReviews.map(lineReviewDtoMapper::toReadLineReviewResp);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<GetUserLineReviewResp> getLineReviewsByNickname(PageRequest pageRequest, UserLineReviewsReq req) {
        Long requestId = userManager.getUserIdByNickname(req.nickname());
        Long currentUserId = authManager.getUserId();

        Slice<MyPageLineReviewProjection> lineReviews = lineReviewManager.findLineReviewsByNickname(requestId, currentUserId, req, pageRequest);
        return lineReviews.map(lineReviewDtoMapper::toGetUserLineReviewResp);
    }

    @Override
    public GetLineReviewRatingInfoResp getRatingLineReviewInfo(Long movieId) {
        RatingLineReviewProjection proj = lineReviewManager.getTotalRatingfInfo(movieId);
        return lineReviewDtoMapper.toGetLineReviewRatingInfoResp(proj);
    }

    @Override
    public GetLineReviewGenderInfoResp getGenderLineReviewInfo(Long movieId) {
        GenderLineReviewProjection proj = lineReviewManager.getGenderRatingfInfo(movieId);
        return lineReviewDtoMapper.toGetLineReviewGenderInfoResp(proj);
    }

}
