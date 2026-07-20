package com.suman.sharecare.auth.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class EmailVerificationException extends RuntimeException {
    private String code;
    public EmailVerificationException(String code, String message) {
        super(message);
        this.code = code;
    }
}
