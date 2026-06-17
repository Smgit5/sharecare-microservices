package com.suman.sharecare.auth.controller;

import com.suman.sharecare.auth.dto.page_dtos.ApiResponseDto;
import com.suman.sharecare.auth.dto.user_dtos.UserResponseDto;
import com.suman.sharecare.auth.dto.user_dtos.UserUpdateRequestDto;
import com.suman.sharecare.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth/users/me")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponseDto> viewProfile(@RequestHeader("X-User-Id") String id) {
        return ResponseEntity.ok(userService.viewProfile(id));
    }

    @PutMapping
    public ResponseEntity<UserResponseDto> updateProfile(@RequestHeader("X-User-Id") String id, @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(userService.updateProfile(id, userUpdateRequestDto));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponseDto> deleteProfile(@RequestHeader("X-User-Id") String id) {
        return ResponseEntity.ok(userService.deleteProfile(id));
    }
}
