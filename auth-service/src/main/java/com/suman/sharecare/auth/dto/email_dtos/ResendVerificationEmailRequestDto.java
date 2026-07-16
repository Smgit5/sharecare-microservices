package com.suman.sharecare.auth.dto.email_dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResendVerificationEmailRequestDto {
    @NotBlank(message = "Email is required")
    private String email;
}
