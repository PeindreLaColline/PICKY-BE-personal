package com.ureca.picky_be.base.implementation.lineReview.mapper;

import com.ureca.picky_be.base.business.lineReview.dto.CreateLineReviewLikeResp;
import com.ureca.picky_be.jpa.entity.lineReview.LineReviewLike;
import org.springframework.stereotype.Component;

@Component
public class LineReviewLikeMapper {


    public CreateLineReviewLikeResp createLineReviewLikeResp(LineReviewLike lineReviewLike) {
        return new CreateLineReviewLikeResp(
                lineReviewLike.getLineReview().getId(),
                lineReviewLike.getUser().getId(),
                lineReviewLike.getPreference().toString()
        );
    }

}
