package com.suman.sharecare.auth.util;

import com.suman.sharecare.auth.dto.role_dtos.RoleResponseDto;
import com.suman.sharecare.auth.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(source = "name", target = "role")
    RoleResponseDto toDto(Role role);
}
