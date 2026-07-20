package com.suman.sharecare.auth.exception;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException {
    private String code;
    public DuplicateResourceException(String code, String message) {
        super(message);
        this.code = code;
    }
}
