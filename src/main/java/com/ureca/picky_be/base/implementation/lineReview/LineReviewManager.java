package com.ureca.picky_be.base.implementation.lineReview;

import com.ureca.picky_be.base.business.lineReview.dto.CreateLineReviewReq;
import com.ureca.picky_be.base.business.lineReview.dto.UpdateLineReviewReq;
import com.ureca.picky_be.base.persistence.UserRepository;
import com.ureca.picky_be.base.persistence.lineReview.LineReviewRepository;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.jpa.lineReview.LineReview;
import com.ureca.picky_be.jpa.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LineReviewManager {

    private final LineReviewRepository lineReviewRepository;
    private final UserRepository userRepository;

    public LineReview createLineReview(CreateLineReviewReq req, Long userId) {
        try {
            if (req.rating() < 0 || req.rating() > 5) {
                throw new CustomException(ErrorCode.LINEREVIEW_INVALID_RATING);
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            boolean exists = lineReviewRepository.existsByMovieIdAndUserId(req.movieId(), user.getId());
            if (exists) {
                throw new CustomException(ErrorCode.LINEREVIEW_CREATE_DUPLICATE);
            }

            LineReview lineReview = LineReview.builder()
                    .userId(userId)
                    .movieId(req.movieId())
                    .rating(req.rating())
                    .context(req.context())
                    .isSpoiler(req.isSpoiler())
                    .build();
            return lineReviewRepository.save(lineReview);
        } catch (CustomException e) {
            throw e;}
        catch (Exception e){
            throw new CustomException(ErrorCode.LINEREVIEW_CREATE_FAILED);
        }

    }

    public LineReview updateLineReview(Long lineReviewId, UpdateLineReviewReq req, Long userId) {
        try{
            LineReview existLineReview = lineReviewRepository.findById(lineReviewId)
                    .orElseThrow(() -> new CustomException(ErrorCode.LINEREVIEW_NOT_FOUND));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            if (!existLineReview.getUserId().equals(userId)) {
                throw new CustomException(ErrorCode.LINEREVIEW_UPDATE_UNAUTHORIZED);
            }
            existLineReview.lineReviewContextUpdate(req.context(), req.isSpoiler());
            return lineReviewRepository.save(existLineReview);
        }catch(Exception e){
            throw new CustomException(ErrorCode.LINEREVIEW_UPDATE_FAILED);
        }
    }
}
