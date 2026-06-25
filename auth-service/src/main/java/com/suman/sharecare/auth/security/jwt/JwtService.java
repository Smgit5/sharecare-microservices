package com.suman.sharecare.auth.security.jwt;

import com.suman.sharecare.auth.repository.UserRepository;
import com.suman.sharecare.auth.service.RefreshTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    private static final long ACCESS_TOKEN_EXPIRY_IN_MINUTES = 2;
    public String generateToken(String username, Set<String> roles, UUID userId) {
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .claim("userId", userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY_IN_MINUTES * 60 * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getUrlDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
