package com.suman.sharecare.auth.dto.user_dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordResetRequestDto {

    @NotBlank(message = "Either username or email is required for sending password reset link.")
    private String username;
}
