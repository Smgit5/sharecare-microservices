package com.suman.sharecare.auth.controller;

import com.suman.sharecare.auth.dto.email_dtos.ResendVerificationEmailRequestDto;
import com.suman.sharecare.auth.dto.page_dtos.ApiResponseDto;
import com.suman.sharecare.auth.dto.user_dtos.*;
import com.suman.sharecare.auth.entity.EmailVerificationToken;
import com.suman.sharecare.auth.service.EmailVerificationService;
import com.suman.sharecare.auth.service.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        userService.register(userRegisterRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequestDto) {
        return ResponseEntity.ok(userService.login(authRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@RequestBody RefreshTokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(userService.refresh(tokenRequestDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto> logout(@RequestBody RefreshTokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(refreshTokenService.logout(tokenRequestDto));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<Void> resendVerificationEmail(@Valid @RequestBody ResendVerificationEmailRequestDto verificationEmailRequestDto) {
        userService.resendVerificationEmail(verificationEmailRequestDto.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<PasswordResetResponseDto> forgotPassword(@Valid @RequestBody PasswordResetRequestDto passwordResetRequestDto) {
        return ResponseEntity.ok(userService.forgotPassword(passwordResetRequestDto));
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<ApiResponseDto> resetPassword(@Valid @RequestBody NewPasswordRequestDto newPasswordRequestDto) {
        userService.resetPassword(newPasswordRequestDto);
        return ResponseEntity.ok(new ApiResponseDto(HttpStatus.OK.value(), "Password has been reset. Please login with the new password."));
    }
}
