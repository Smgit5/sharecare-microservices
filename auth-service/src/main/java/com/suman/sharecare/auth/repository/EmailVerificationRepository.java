package com.suman.sharecare.auth.repository;

import com.suman.sharecare.auth.entity.EmailVerificationToken;
import com.suman.sharecare.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerificationToken, UUID> {
    Optional<EmailVerificationToken> findByToken(String token);

    @Query("""
            SELECT e.token FROM EmailVerificationToken e
            WHERE e.user = :user
            AND e.used = false
            AND e.expiry > :currentTime
""")
    Optional<String> findUsableToken(User user, LocalDateTime currentTime);
}
