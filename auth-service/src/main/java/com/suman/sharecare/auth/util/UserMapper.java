package com.suman.sharecare.auth.util;

import com.suman.sharecare.auth.dto.user_dtos.UserRegisterRequestDto;
import com.suman.sharecare.auth.dto.user_dtos.UserResponseDto;
import com.suman.sharecare.auth.dto.user_dtos.UserUpdateRequestDto;
import com.suman.sharecare.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
    UserResponseDto toDto(User user);
    User toEntity(UserRegisterRequestDto dto);
    void updateToEntity(UserUpdateRequestDto dto, @MappingTarget User user);
}
