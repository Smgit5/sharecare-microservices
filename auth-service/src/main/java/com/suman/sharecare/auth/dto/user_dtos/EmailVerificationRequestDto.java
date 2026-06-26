package com.suman.sharecare.auth.dto.user_dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerificationRequestDto {
    @NotBlank(message = "Email verification token is required.")
    private String requestToken;
}
