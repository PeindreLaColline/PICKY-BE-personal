package com.ureca.picky_be.global.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse implements Serializable {
    private String status;
    private T body;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiResponse(String status, T body) {
        this.status = status;
        this.body = body;
    }
}