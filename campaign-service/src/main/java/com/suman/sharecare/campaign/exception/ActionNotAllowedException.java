package com.suman.sharecare.campaign.exception;

public class ActionNotAllowedException extends RuntimeException {
    public ActionNotAllowedException(String message) {
        super(message);
    }
}
