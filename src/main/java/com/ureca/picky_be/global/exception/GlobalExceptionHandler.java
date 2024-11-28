package com.ureca.picky_be.global.exception;

import com.ureca.picky_be.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity
                .status(ErrorCode.VALIDATION_FAILED.getHttpStatus())
                .body(new ApiResponse<>(
                        false,
                        ErrorCode.VALIDATION_FAILED.getHttpStatus().value(),
                        ErrorCode.VALIDATION_FAILED.getMessage(),
                        errors
                ));
    }


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ApiResponse<>(
                        false,
                        errorCode.getHttpStatus().value(),
                        errorCode.getMessage(),
                        null
                ));
    }

    // 예상치 못한 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(new ApiResponse<>(
                        false,
                        ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value(),
                        "예상치 못한 문제가 발생했습니다. 관리자에게 문의하세요.",
                        null
                ));
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity
                .status(ErrorCode.METHOD_NOT_ALLOWED.getHttpStatus())
                .body(new ApiResponse<>(
                        false,
                        ErrorCode.METHOD_NOT_ALLOWED.getHttpStatus().value(),
                        "지원하지 않는 요청 메서드입니다.",
                        null
                ));
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingPathVariable(MissingPathVariableException ex) {
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getHttpStatus())
                .body(new ApiResponse<>(
                        false,
                        ErrorCode.BAD_REQUEST.getHttpStatus().value(),
                        String.format("필수 경로 변수 '%s'가 누락되었습니다.", ex.getVariableName()),
                        null
                ));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingRequestParameter(MissingServletRequestParameterException ex) {
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getHttpStatus())
                .body(new ApiResponse<>(
                        false,
                        ErrorCode.BAD_REQUEST.getHttpStatus().value(),
                        String.format("필수 요청 파라미터 '%s'가 누락되었습니다.", ex.getParameterName()),
                        null
                ));
        }
    }


