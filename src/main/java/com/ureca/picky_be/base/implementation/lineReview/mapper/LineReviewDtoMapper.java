package com.ureca.picky_be.base.implementation.lineReview.mapper;

import com.ureca.picky_be.base.business.lineReview.dto.*;
import com.ureca.picky_be.jpa.lineReview.LineReview;
import org.springframework.stereotype.Component;

@Component
public class LineReviewDtoMapper {


    public CreateLineReviewResp createLineReviewResp(LineReview lineReview) {
        return new CreateLineReviewResp(
                lineReview.getUserId(),
                lineReview.getWriterNickname(),
                lineReview.getMovieId(),
                lineReview.getRating(),
                lineReview.getContext(),
                lineReview.getIsSpoiler()
        );
    }

    public UpdateLineReviewResp updateLineReview(LineReview lineReview) {
        return new UpdateLineReviewResp(
                lineReview.getId(),
                lineReview.getWriterNickname(),
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
                projection.getWriterNickname(),
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

    public GetUserLineReviewResp toGetUserLineReviewResp(MyPageLineReviewProjection projection) {
        return new GetUserLineReviewResp(
                projection.getId(),
                projection.getWriterNickname(),
                projection.getUserId(),
                projection.getRating(),
                projection.getContext(),
                projection.getIsSpoiler(),
                projection.getLikes(),
                projection.getDislikes(),
                projection.getCreatedAt(),
                new LineReviewMovieInfo(
                    projection.getMovieId(),
                    projection.getMovieTitle(),
                    projection.getMoviePosterUrl()
                )
        );
    }
}
