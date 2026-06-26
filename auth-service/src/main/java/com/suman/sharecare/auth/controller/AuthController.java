package com.suman.sharecare.auth.controller;

import com.suman.sharecare.auth.dto.page_dtos.ApiResponseDto;
import com.suman.sharecare.auth.dto.user_dtos.*;
import com.suman.sharecare.auth.service.EmailVerificationService;
import com.suman.sharecare.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<EmailVerificationResponseDto> register(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(userRegisterRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequestDto) {
        return ResponseEntity.ok(userService.login(authRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@RequestBody RefreshTokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(userService.refresh(tokenRequestDto));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponseDto> verifyEmail(@Valid @RequestBody EmailVerificationRequestDto emailVerificationRequestDto, @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(userService.verifyEmail(emailVerificationRequestDto, userId));
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<EmailVerificationResponseDto> resendVerificationEmail(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(userService.resendVerificationEmail(userId));
    }
}
