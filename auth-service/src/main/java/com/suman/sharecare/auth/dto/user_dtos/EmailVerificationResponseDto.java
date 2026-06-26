package com.suman.sharecare.auth.dto.user_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailVerificationResponseDto {
    private String emailVerificationToken;
}
