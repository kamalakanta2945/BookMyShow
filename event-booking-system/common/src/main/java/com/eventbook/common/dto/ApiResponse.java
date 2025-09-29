package com.eventbook.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String message;
    private T data;
    private boolean success;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.success = true;
    }

    public ApiResponse(String message) {
        this.message = message;
        this.success = true;
    }
}