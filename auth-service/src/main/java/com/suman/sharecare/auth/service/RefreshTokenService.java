package com.suman.sharecare.auth.service;

import com.suman.sharecare.auth.dto.page_dtos.ApiResponseDto;
import com.suman.sharecare.auth.dto.user_dtos.RefreshTokenRequestDto;
import com.suman.sharecare.auth.entity.RefreshToken;
import com.suman.sharecare.auth.entity.User;
import com.suman.sharecare.auth.exception.ActionNotAllowedException;
import com.suman.sharecare.auth.exception.ResourceNotFoundException;
import com.suman.sharecare.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final long REFRESH_TOKEN_EXPIRY_IN_MINUTES = 60;
    private final RefreshTokenRepository refreshTokenRepository;

    public String generateRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiry(LocalDateTime.now().plusMinutes(REFRESH_TOKEN_EXPIRY_IN_MINUTES));
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);
        return savedRefreshToken.getToken();
    }

    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Refresh token not found!"));
        if(refreshToken.isRevoked()) {
            throw new ActionNotAllowedException("Refresh token has been revoked");
        }
        if(refreshToken.getExpiry().isBefore(LocalDateTime.now())) {
            throw new ActionNotAllowedException("Refresh token has expired!");
        }
        return refreshToken;
    }

    public void revokeRefreshToken(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    public ApiResponseDto logout(RefreshTokenRequestDto tokenRequestDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenRequestDto.getToken()).orElseThrow(() -> new ResourceNotFoundException("Token not found!"));
        if(refreshToken.isRevoked()) {
            throw new ActionNotAllowedException("Token is already revoked.");
        }
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
        return new ApiResponseDto(HttpStatus.OK.value(), "You have been logged out.");
    }
}
