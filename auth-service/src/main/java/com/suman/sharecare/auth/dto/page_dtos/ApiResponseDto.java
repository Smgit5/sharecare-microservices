package com.suman.sharecare.auth.dto.page_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponseDto {
    private int status;
    private String code;
    private String message;

    public ApiResponseDto(int status, String message) {
        this(status, "SUCCESS", message);
    }

    public ApiResponseDto(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
