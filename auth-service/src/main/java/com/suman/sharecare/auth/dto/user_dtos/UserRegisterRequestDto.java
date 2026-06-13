package com.suman.sharecare.auth.dto.user_dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequestDto {

    @NotNull(message = "Username is required")
    @Pattern(
            regexp = "^[A-Za-z0-9_-]{3,15}$",
            message = "Username must be 3-15 characters long and contain only letters, digits and underscore"
    )
    private String username;

    @NotNull(message = "Password is required")
    @Pattern(
            regexp = "^(?=\\S{8,15}$)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character and be 8-15 characters long and must not contain whitespace"
    )
    private String password;
}
