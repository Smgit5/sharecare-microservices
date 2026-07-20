package com.suman.sharecare.auth.exception;

import com.suman.sharecare.auth.dto.page_dtos.ApiResponseDto;
import com.suman.sharecare.auth.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponseDto> handleDuplicateResourceException(DuplicateResourceException ex) {
        log.error("Inside GlobalExceptionHandler :: handleDuplicateResourceException, msg = {}", ex.getMessage());
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.CONFLICT.value(), ex.getCode(), "Duplicate resource");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponseDto);
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<ApiResponseDto> handleRefreshTokenExpiredException(RefreshTokenInvalidException ex) {
        log.error("Inside GlobalExceptionHandler :: handleRefreshTokenExpiredException, msg = {}", ex.getMessage());
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.UNAUTHORIZED.value(), ErrorCode.SESSION_EXPIRED.name(), "Your session has expired. Please login.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponseDto);
    }

    @ExceptionHandler(EmailVerificationException.class)
    public ResponseEntity<ApiResponseDto> handleEmailVerificationException(EmailVerificationException ex) {
        log.warn("Inside GlobalExceptionHandler :: handleEmailVerificationException, msg = {}", ex.getMessage());
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.UNAUTHORIZED.value(), ex.getCode(), "Authentication error");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponseDto);
    }

    @ExceptionHandler(PasswordResetTokenInvalidException.class)
    public ResponseEntity<ApiResponseDto> handlePasswordResetTokenInvalidException(PasswordResetTokenInvalidException ex) {
        log.warn("Inside GlobalExceptionHandler :: handlePasswordResetTokenInvalidException, msg = {}", ex.getMessage());
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.UNAUTHORIZED.value(), ErrorCode.PASSWORD_RESET_LINK_INVALID.name(), "Authentication error");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponseDto);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponseDto> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Inside GlobalExceptionHandler :: handleAuthenticationException, msg = {}", ex.getMessage());
        String code = ErrorCode.UNKNOWN_AUTH_ERROR.name();
        if(ex instanceof BadCredentialsException) {
            code = ErrorCode.BAD_CREDENTIALS.name();
        }
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.UNAUTHORIZED.value(), code, "Authentication error");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponseDto);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Inside GlobalExceptionHandler :: handleResourceNotFoundException, msg = {}", ex.getMessage());

        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.NOT_FOUND.value(), ErrorCode.RESOURCE_NOT_FOUND.name(), ex.getMessage());
        return ResponseEntity.status(apiResponseDto.getStatus()).body(apiResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Inside GlobalExceptionHandler :: handleValidationException, msg = {}", ex.getMessage());
        FieldError fieldError = ex.getBindingResult().getFieldErrors().getFirst();
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), ErrorCode.VALIDATION_ERROR.name(), fieldError.getDefaultMessage());
        return ResponseEntity.badRequest().body(apiResponseDto);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleUnknownException(Exception ex) {
        log.error("Inside GlobalExceptionHandler :: handleUnknownException :", ex);

        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.UNKNOWN_ERROR.name(),"Something went wrong!");
        return ResponseEntity.internalServerError().body(apiResponseDto);
    }
}
