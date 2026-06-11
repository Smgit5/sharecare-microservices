package com.suman.sharecare.campaign.dto.ErrorDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ValidationErrorResponseDto {
    private int status;
    private Map<String, String> errors;
}
