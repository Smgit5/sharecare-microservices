package com.suman.sharecare.auth.service;

import com.suman.sharecare.auth.dto.user_dtos.RefreshTokenRequestDto;
import com.suman.sharecare.auth.entity.RefreshToken;
import com.suman.sharecare.auth.entity.User;
import com.suman.sharecare.auth.exception.RefreshTokenInvalidException;
import com.suman.sharecare.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private static final long REFRESH_TOKEN_EXPIRY_IN_MINUTES = 4; //43200
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
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new RefreshTokenInvalidException("Refresh token not found!"));
        if(refreshToken.isRevoked()) {
            throw new RefreshTokenInvalidException("Refresh token has been revoked.");
        }
        if(refreshToken.getExpiry().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenInvalidException("Refresh token is expired.");
        }
        return refreshToken;
    }

    public void revokeRefreshToken(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    public void logout(RefreshTokenRequestDto tokenRequestDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenRequestDto.getToken()).orElse(null);
        if(refreshToken == null) {
            log.warn("Inside RefreshTokenService :: logout - Refresh token not found.");
            return;
        }
        if(refreshToken.isRevoked()) {
            log.warn("Inside RefreshTokenService :: logout - Token is already revoked.");
            return;
        }
        revokeRefreshToken(refreshToken);
    }

    public void deleteRefreshTokensByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
