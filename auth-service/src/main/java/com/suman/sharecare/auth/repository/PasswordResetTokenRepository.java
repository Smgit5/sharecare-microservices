package com.suman.sharecare.auth.repository;

import com.suman.sharecare.auth.entity.PasswordResetToken;
import com.suman.sharecare.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByToken(String token);

    @Query("""
            SELECT p FROM PasswordResetToken p
            WHERE p.user = :user
            AND p.used = false
            AND p.expiry > :currentTime
""")
    Optional<PasswordResetToken> findUsableToken(User user, LocalDateTime currentTime);
}