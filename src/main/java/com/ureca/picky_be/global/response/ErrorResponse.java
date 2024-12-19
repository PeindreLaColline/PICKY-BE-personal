package com.ureca.picky_be.global.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private boolean success; // 항상 false
    private int code;        // HTTP 상태 코드
    private String message;  // 에러 메시지
    private String errorCode; // 에러 코드
}
