package com.suman.sharecare.auth.dto.role_dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoleResponseDto {
    private UUID id;
    private String role;
}
