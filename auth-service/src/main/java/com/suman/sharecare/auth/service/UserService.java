package com.suman.sharecare.auth.service;

import com.suman.sharecare.auth.dto.page_dtos.ApiResponseDto;
import com.suman.sharecare.auth.dto.user_dtos.UserRegisterRequestDto;
import com.suman.sharecare.auth.entity.Role;
import com.suman.sharecare.auth.entity.User;
import com.suman.sharecare.auth.repository.UserRepository;
import com.suman.sharecare.auth.util.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
