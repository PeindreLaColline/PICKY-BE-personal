package com.ureca.picky_be.global.exception;

import com.ureca.picky_be.base.implementation.lineReview.mapper.LineReviewMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCode {
    // 기본 에러
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "E001", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "E002", "인증에 실패했습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "E003", "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "E004", "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E005", "서버에 문제가 발생했습니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "E006", "입력 값이 유효하지 않습니다."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "E007", "유효성 검사가 실패했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "E008", "지원하지 않는 요청 메서드입니다."),

    // USER
    USER_SAVE_FAILED(HttpStatus.BAD_REQUEST, "USR001", "유저 저장에 실패했습니다."),
    USER_DELETE_FAILED(HttpStatus.BAD_REQUEST, "USR002", "유저 삭제에 실패했습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USR003", "해당 유저가 존재하지 않습니다."),

    // MOVIE
    MOVIE_NOT_FOUND(HttpStatus.NOT_FOUND, "MOV001", "해당 영화가 존재하지 않습니다."),

    // BOARD
    BOARD_USER_NOT_WRITER(HttpStatus.UNAUTHORIZED, "BOD001", "해당 게시물 작성자와 일치하지 않습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOD002", "해당 게시물이 존재하지 않습니다."),

    //LineReview
    LINEREVIEW_CREATE_FAILED(HttpStatus.BAD_REQUEST, "LR001", "한줄평 작성에 실패했습니다."),
    LINEREVIEW_INVALID_RATING(HttpStatus.BAD_REQUEST, "LR002", "평점 값은 0에서 5 사이입니다" ),
    LINEREVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "LR003", "한줄평을 찾을 수 없습니다." ),
    LINEREVIEW_UPDATE_UNAUTHORIZED(HttpStatus.BAD_REQUEST, "LR004", "수정 권한이 없습니다." ),
    LINEREVIEW_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "LR005", "한줄평 수정에 실패 했습니다." ),
    LINEREVIEW_CREATE_DUPLICATE(HttpStatus.BAD_REQUEST, "LR006", "한줄평은 계정당 한개만 작성가능합니다." ),

    //LineReviewLike
    INVALID_PREFERENCE(HttpStatus.BAD_REQUEST, "LRL001","LIKE, DISLIKE값만 가능합니다"),
    LINEREVIEWLIKE_CREATE_FAILED(HttpStatus.BAD_REQUEST, "LRL002","한줄평 좋아요에 실패했습니다."),
    LINEREVIEWLIKE_CREATE_DUPLICATE(HttpStatus.BAD_REQUEST,"LRL003","좋아요나 싫어요는 한번만 가능합니다."),
    LINEREVIEW_COUNT_FAILED(HttpStatus.BAD_REQUEST, "LRL004", "좋아요 수 찾기 실패 했습니다."),
    LINEREVIEWLIKE_SELF_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "LRL005", "자신의 한줄평에는 좋아요가 안됩니다." );

    private final HttpStatus httpStatus; // HTTP 상태 코드
    private final String code;          // 에러 코드
    private final String message;       // 에러 메시지

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
