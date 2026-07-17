package com.suman.sharecare.auth.service;

import com.suman.sharecare.auth.entity.PasswordResetToken;
import com.suman.sharecare.auth.entity.User;
import com.suman.sharecare.auth.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private static final long PASSWORD_RESET_TOKEN_EXPIRY_IN_MINUTES = 2;

    private PasswordResetToken generateNewPasswordResetToken(User user) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setExpiry(LocalDateTime.now().plusMinutes(PASSWORD_RESET_TOKEN_EXPIRY_IN_MINUTES));
        PasswordResetToken savedToken = passwordResetTokenRepository.save(passwordResetToken);
        return savedToken;
    }

    public PasswordResetToken reuseOrGenerateToken(User user, LocalDateTime currentTime) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findUsableToken(user, LocalDateTime.now());
        return passwordResetToken.orElseGet(() -> generateNewPasswordResetToken(user));
    }
}
