package com.suman.sharecare.auth.dto.user_dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequestDto {
    private String token;
}
