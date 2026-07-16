package com.suman.sharecare.auth.dto.user_dtos;

import com.suman.sharecare.auth.dto.role_dtos.RoleResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserResponseDto {
    private UUID id;
    private String username;
    private String email;
    private Set<RoleResponseDto> roles;
}
