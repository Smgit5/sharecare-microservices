package com.suman.sharecare.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class RefreshTokenInvalidException extends RuntimeException {
    public RefreshTokenInvalidException(String message) {
        super(message);
    }
}
