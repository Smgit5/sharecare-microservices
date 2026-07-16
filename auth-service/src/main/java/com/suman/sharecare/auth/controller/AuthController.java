package com.suman.sharecare.auth.controller;

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
        EmailVerificationToken emailVerificationToken = userService.register(userRegisterRequestDto);
        userService.sendEmailforEmailVerification(emailVerificationToken);
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
    public ResponseEntity<ApiResponseDto> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponseDto(HttpStatus.OK.value(), "Your email has been verified. You can login now."));
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<EmailVerificationResponseDto> resendVerificationEmail(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(userService.resendVerificationEmail(userId));
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
