package com.suman.sharecare.gateway.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getUrlDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractSubject(Claims claims) {
        return claims.getSubject();
    }

    public String extractUserId(Claims claims) {
        return claims.get("userId", String.class);
    }

    public Set<String> extractRoles(Claims claims) {
        List<String> roleList = claims.get("roles", List.class);
        return new HashSet<>(roleList);
    }

    private Date extractExpiration(Claims claims) {
        return claims.getExpiration();
    }

    private boolean isTokenExpired(Claims claims) {
        return extractExpiration(claims).before(new Date());
    }

    public boolean isTokenValid(Claims claims) { // currently only checks for token expiration. Later may add other checks too.
        return !isTokenExpired(claims);
    }
}
