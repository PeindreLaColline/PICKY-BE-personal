package com.ureca.picky_be.global.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.picky_be.global.exception.GlobalExceptionHandler;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import com.fasterxml.jackson.annotation.JsonInclude;

@ControllerAdvice
@RestControllerAdvice(
        basePackages = "com.ureca.picky_be.base",
        basePackageClasses = GlobalExceptionHandler.class
)
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    public ApiResponseAdvice() {
        this.objectMapper = new ObjectMapper();
        // null 값을 가진 필드는 JSON 응답에서 제외
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true; // 모든 컨버터에 대해 적용
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        try {
            // ApiResponse 타입은 그대로 반환
            if (body instanceof ApiResponse) {
                return body;
            }

            // String 응답은 JSON 문자열로 변환
            if (body instanceof String) {
                return objectMapper.writeValueAsString(
                        ApiResponse.successWithData("요청이 성공적으로 처리되었습니다.", body)
                );
            }

            // ResponseEntity 응답 처리
            if (body instanceof ResponseEntity) {
                ResponseEntity<?> entity = (ResponseEntity<?>) body;
                return ResponseEntity
                        .status(entity.getStatusCode())
                        .body(ApiResponse.successWithData("요청이 성공적으로 처리되었습니다.", entity.getBody()));
            }

            // null 응답 처리
            if (body == null) {
                return ApiResponse.successWithoutData("요청이 성공적으로 처리되었습니다.");
            }

            // 일반 객체는 ApiResponse로 감싸기
            return ApiResponse.successWithData("요청이 성공적으로 처리되었습니다.", body);

        } catch (Exception e) {
            throw new RuntimeException("Error wrapping response", e);
        }
    }
}
