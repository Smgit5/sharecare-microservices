package com.suman.sharecare.auth.dto.user_dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NewPasswordRequestDto {

    @NotBlank(message = "token is required for resetting the password")
    private String token;

    @NotNull(message = "Password is required")
    @Pattern(
            regexp = "^(?=\\S{8,15}$)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character and be 8-15 characters long and must not contain whitespace"
    )
    private String newPassword;

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }
}
