package com.suman.sharecare.auth.dto.page_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ValidationErrorResponseDto {
    private int status;
    private Map<String, String> error;
}
