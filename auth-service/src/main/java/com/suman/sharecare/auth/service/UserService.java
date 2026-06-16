package com.suman.sharecare.auth.service;

import com.suman.sharecare.auth.dto.page_dtos.ApiResponseDto;
import com.suman.sharecare.auth.dto.user_dtos.UserRegisterRequestDto;
import com.suman.sharecare.auth.dto.user_dtos.UserResponseDto;
import com.suman.sharecare.auth.dto.user_dtos.UserUpdateRequestDto;
import com.suman.sharecare.auth.entity.Role;
import com.suman.sharecare.auth.entity.User;
import com.suman.sharecare.auth.exception.ResourceNotFoundException;
import com.suman.sharecare.auth.repository.UserRepository;
import com.suman.sharecare.auth.util.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public ApiResponseDto register(UserRegisterRequestDto userRegisterRequestDto) {
        User user = userMapper.toEntity(userRegisterRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleService.getRoleByName(RoleService.CITIZEN);
        user.setRole(role);
        userRepository.save(user);
        return new ApiResponseDto(HttpStatus.CREATED.value(), "User saved.");
    }

    public UserResponseDto viewProfile(String id) {
        User user = userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        return userMapper.toDto(user);
    }

    public UserResponseDto updateProfile(String id, UserUpdateRequestDto userUpdateRequestDto) {
        User existingUser = userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        userMapper.updateToEntity(userUpdateRequestDto, existingUser);
        return userMapper.toDto(userRepository.save(existingUser));
    }

    public ApiResponseDto deleteProfile(String id) {
        userRepository.deleteById(UUID.fromString(id));
        return new ApiResponseDto(HttpStatus.OK.value(), "Your account has been deleted.");
    }
}
