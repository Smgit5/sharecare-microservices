package com.suman.sharecare.auth.service;

import com.suman.sharecare.auth.entity.EmailVerificationToken;
import com.suman.sharecare.auth.entity.User;
import com.suman.sharecare.auth.exception.ActionNotAllowedException;
import com.suman.sharecare.auth.exception.ResourceNotFoundException;
import com.suman.sharecare.auth.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private static final long EMAIL_VERIFICATION_EXPIRY_IN_MINUTES = 10;
    private final EmailVerificationRepository emailVerificationRepository;

    public EmailVerificationToken generateEmailVerificationToken(User user) {
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken(UUID.randomUUID().toString());
        emailVerificationToken.setUser(user);
        emailVerificationToken.setExpiry(LocalDateTime.now().plusMinutes(EMAIL_VERIFICATION_EXPIRY_IN_MINUTES));
        EmailVerificationToken savedToken = emailVerificationRepository.save(emailVerificationToken);
        return savedToken;
    }

    public EmailVerificationToken verifyEmail(String token) {
        EmailVerificationToken emailVerificationToken = emailVerificationRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Token not found!"));
        if(emailVerificationToken.getExpiry().isBefore(LocalDateTime.now())) {
            throw new ActionNotAllowedException("Token has expired. Please resend verify email request.");
        }
        if(emailVerificationToken.isUsed()) {
            throw new ActionNotAllowedException("Token has already been used.");
        }
        emailVerificationToken.setUsed(true);
        return emailVerificationToken;
    }

    public EmailVerificationToken reuseOrGenerateToken(User user, LocalDateTime currentTime) {
        Optional<EmailVerificationToken> verificationToken = emailVerificationRepository.findUsableToken(user, currentTime);
        return verificationToken.orElseGet(() -> generateEmailVerificationToken(user));
    }
}
