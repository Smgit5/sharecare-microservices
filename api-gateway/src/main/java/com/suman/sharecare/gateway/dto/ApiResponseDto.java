package com.suman.sharecare.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponseDto {
    private int status;
    private String message;
}
