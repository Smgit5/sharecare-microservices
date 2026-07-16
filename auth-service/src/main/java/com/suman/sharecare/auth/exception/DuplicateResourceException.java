package com.suman.sharecare.auth.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DuplicateResourceException extends DataIntegrityViolationException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
