package com.suman.sharecare.auth.controller;

import com.suman.sharecare.auth.dto.page_dtos.ApiResponseDto;
import com.suman.sharecare.auth.dto.user_dtos.AuthRequestDto;
import com.suman.sharecare.auth.dto.user_dtos.AuthResponseDto;
import com.suman.sharecare.auth.dto.user_dtos.RefreshTokenRequestDto;
import com.suman.sharecare.auth.dto.user_dtos.UserRegisterRequestDto;
import com.suman.sharecare.auth.entity.RefreshToken;
import com.suman.sharecare.auth.entity.Role;
import com.suman.sharecare.auth.entity.User;
import com.suman.sharecare.auth.security.jwt.JwtService;
import com.suman.sharecare.auth.service.RefreshTokenService;
import com.suman.sharecare.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> register(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(userRegisterRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword())
        );
        User user = (User) (authentication.getPrincipal());
        UUID userId = user.getId();
        String username = user.getUsername();
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        String accessToken = jwtService.generateToken(username, roles, userId);
        String refreshToken = refreshTokenService.generateRefreshToken(user);
        return ResponseEntity.ok(new AuthResponseDto(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@RequestBody RefreshTokenRequestDto tokenRequestDto) {
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(tokenRequestDto.getToken());
        refreshTokenService.revokeRefreshToken(refreshToken);
        User user = refreshToken.getUser();
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        String newRefreshToken = refreshTokenService.generateRefreshToken(user);
        String newAccessToken = jwtService.generateToken(user.getUsername(), roles, user.getId());
        return ResponseEntity.ok(new AuthResponseDto(newAccessToken, newRefreshToken));
    }
}
