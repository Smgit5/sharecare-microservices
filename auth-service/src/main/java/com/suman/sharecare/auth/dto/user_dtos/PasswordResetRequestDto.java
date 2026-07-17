package com.suman.sharecare.auth.dto.user_dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordResetRequestDto {

    @NotBlank(message = "Email is required")
    private String email;
}
