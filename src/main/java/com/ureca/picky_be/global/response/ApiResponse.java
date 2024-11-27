package com.ureca.picky_be.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    // 성공 응답 생성 (데이터 없음)
    public static ApiResponse<Void> successWithoutData(String message) {
        return new ApiResponse<>(true, 200, message, null);
    }

    // 성공 응답 생성 (데이터 있음)
    public static <T> ApiResponse<T> successWithData(String message, T data) {
        return new ApiResponse<>(true, 200, message, data);
    }

    // 실패 응답 생성
    public static ApiResponse<Void> failure(int code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }// 실제 데이터
}

