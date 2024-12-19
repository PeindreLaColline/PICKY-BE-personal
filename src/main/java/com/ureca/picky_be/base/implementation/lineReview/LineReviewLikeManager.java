package com.ureca.picky_be.base.implementation.lineReview;

import com.ureca.picky_be.base.business.lineReview.dto.CountLineReviewLikeResp;
import com.ureca.picky_be.base.business.lineReview.dto.CreateLineReviewLikeReq;
import com.ureca.picky_be.base.persistence.user.UserRepository;
import com.ureca.picky_be.base.persistence.lineReview.LineReviewLikeRepository;
import com.ureca.picky_be.base.persistence.lineReview.LineReviewRepository;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.entity.lineReview.LineReview;
import com.ureca.picky_be.jpa.entity.lineReview.LineReviewLike;
import com.ureca.picky_be.jpa.entity.lineReview.Preference;
import com.ureca.picky_be.jpa.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LineReviewLikeManager {

    private final LineReviewRepository lineReviewRepository;
    private final LineReviewLikeRepository lineReviewLikeRepository;
    private final UserRepository userRepository;

    public SuccessCode createLineReviewLike(CreateLineReviewLikeReq req, Long userId) {
        LineReview lineReview = lineReviewRepository.findById(req.lineReviewId())
                .orElseThrow(() -> new CustomException(ErrorCode.LINEREVIEW_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (lineReview.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.LINEREVIEWLIKE_SELF_NOT_ALLOWED);
        }

        Preference preferenceEnum = Preference.fromString(req.preference());
        Optional<LineReviewLike> existingLikeOpt = lineReviewLikeRepository.findByLineReviewIdAndUserId(req.lineReviewId(), user.getId());

        if (existingLikeOpt.isPresent()) {
            LineReviewLike existingLike = existingLikeOpt.get();
            if (existingLike.getPreference() == preferenceEnum) {
                lineReviewLikeRepository.delete(existingLike);
                return SuccessCode.DELETE_LINE_REVIEW_LIKE;
            } else {
                existingLike.updatePreference(preferenceEnum);
                lineReviewLikeRepository.save(existingLike);
                return SuccessCode.UPDATE_LINE_REVIEW_LIKE;
            }
        }

        LineReviewLike lineReviewLike = LineReviewLike.of(lineReview, user, preferenceEnum);
        lineReviewLikeRepository.save(lineReviewLike);
        return SuccessCode.CREATE_LINE_REVIEW_LIKE;
    }

    public CountLineReviewLikeResp countLineReviewLike(Long lineReviewId) {
        try {
            // 좋아요 수를 계산
            Integer likeCount = lineReviewLikeRepository.countLikesByLineReviewId(lineReviewId);

            return new CountLineReviewLikeResp(lineReviewId, likeCount);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.LINEREVIEW_COUNT_FAILED);
        }
    }
}
