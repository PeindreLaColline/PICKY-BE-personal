package com.ureca.picky_be.base.implementation.lineReview;

import com.ureca.picky_be.base.business.lineReview.dto.CreateLineReviewReq;
import com.ureca.picky_be.base.business.lineReview.dto.UpdateLineReviewReq;
import com.ureca.picky_be.base.persistence.user.UserRepository;
import com.ureca.picky_be.base.persistence.lineReview.LineReviewRepository;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.jpa.lineReview.LineReview;
import com.ureca.picky_be.jpa.lineReview.SortType;
import com.ureca.picky_be.jpa.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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


    public Slice<LineReviewProjection> findLineReviewsByMovie(LineReviewQueryRequest queryReq, PageRequest pageRequest) {
        try {
            validateCursor(queryReq.lastReviewId(), queryReq.lastCreatedAt());

            if (queryReq.sortType() == SortType.LIKES) {
                return lineReviewRepository.findByMovieAndLikesCursor(
                        queryReq.movieId(),
                        queryReq.lastReviewId(),
                        pageRequest
                );
            } else if (queryReq.sortType() == SortType.LATEST) {
                return lineReviewRepository.findByMovieAndLatestCursor(
                        queryReq.movieId(),
                        queryReq.lastReviewId(),
                        pageRequest
                );
            } else {
                throw new CustomException(ErrorCode.LINEREVIEW_CREATE_FAILED);
            }
        } catch (CustomException e) {
            // 요청 매개변수 오류
            throw e;
        } catch (Exception e) {
            // 일반적인 예외 처리
            throw new CustomException(ErrorCode.LINEREVIEW_GET_FAILED);
        }
    }


    private void validateCursor(Long lastReviewId, LocalDateTime lastCreatedAt) {
        // 커서가 없는 경우 (첫 요청)
        if (lastReviewId == null && lastCreatedAt == null) {
            return;
        }

        // ID 또는 날짜 중 하나만 제공된 경우
        if (lastReviewId == null || lastCreatedAt == null) {
            throw new CustomException(ErrorCode. LINEREVIEW_INVALID_CURSOR1);
        }

        // ID가 유효하지 않은 경우
        if (lastReviewId <= 0) {
            throw new CustomException(ErrorCode. LINEREVIEW_INVALID_CURSOR2);
        }

        // 날짜가 유효하지 않은 경우
        if (lastCreatedAt.isAfter(LocalDateTime.now())) {
            throw new CustomException(ErrorCode. LINEREVIEW_INVALID_CURSOR3);
        }
    }
}


