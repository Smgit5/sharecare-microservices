package com.suman.sharecare.auth.dto.page_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponseDto {
    private int status;
    private String message;
}
