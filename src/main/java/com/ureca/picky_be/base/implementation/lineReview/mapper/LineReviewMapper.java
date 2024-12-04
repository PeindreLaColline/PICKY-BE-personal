package com.ureca.picky_be.base.implementation.lineReview.mapper;

import com.ureca.picky_be.base.business.lineReview.dto.CreateLineReviewResp;
import com.ureca.picky_be.base.business.lineReview.dto.LineReviewProjection;
import com.ureca.picky_be.base.business.lineReview.dto.ReadLineReviewResp;
import com.ureca.picky_be.base.business.lineReview.dto.UpdateLineReviewResp;
import com.ureca.picky_be.jpa.lineReview.LineReview;
import org.springframework.stereotype.Component;

@Component
public class LineReviewMapper {


    public CreateLineReviewResp createLineReviewResp(LineReview lineReview) {
        return new CreateLineReviewResp(
                lineReview.getUserId(),
                lineReview.getMovieId(),
                lineReview.getRating(),
                lineReview.getContext(),
                lineReview.getIsSpoiler()
        );
    }

    public UpdateLineReviewResp updateLineReview(LineReview lineReview) {
        return new UpdateLineReviewResp(
                lineReview.getId(),
                lineReview.getUserId(),
                lineReview.getMovieId(),
                lineReview.getRating(),
                lineReview.getContext(),
                lineReview.getIsSpoiler()
        );
    }

    public ReadLineReviewResp toReadLineReviewResp(LineReviewProjection projection) {
        return new ReadLineReviewResp(
                projection.getId(),
                projection.getUserId(),
                projection.getMovieId(),
                projection.getRating(),
                projection.getContext(),
                projection.getIsSpoiler(),
                projection.getLikes(),
                projection.getDislikes(),
                projection.getCreatedAt()
        );
    }
}
