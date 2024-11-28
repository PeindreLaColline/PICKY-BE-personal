package com.ureca.picky_be.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ureca.picky_be.global.success.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success; // 요청 성공 여부
    private int code;        // HTTP 상태 코드
    private String message;  // 응답 메시지
    private T data;

    // 성공 응답 생성 (SuccessCode 사용, 데이터 없음)
    public static ApiResponse<Void> success(SuccessCode successCode) {
        return new ApiResponse<>(true, successCode.getStatus(), successCode.getMessage(), null);
    }

    // 성공 응답 생성 (SuccessCode 사용, 데이터 있음)
    public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
        return new ApiResponse<>(true, successCode.getStatus(), successCode.getMessage(), data);
    }

    // 실패 응답 생성
    public static ApiResponse<Void> failure(int code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }
}

