package com.suman.sharecare.auth.exception;

import com.suman.sharecare.auth.dto.page_dtos.ApiResponseDto;
import com.suman.sharecare.auth.dto.page_dtos.ValidationErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Inside GlobalExceptionHandler :: handleDataIntegrityViolationException : {}", ex.getMessage());
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.CONFLICT.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponseDto);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ApiResponseDto> handleRefreshTokenExpiredException(RefreshTokenExpiredException ex) {
        log.error("Inside GlobalExceptionHandler :: handleRefreshTokenExpiredException : {}", ex.getMessage());
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponseDto);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponseDto> handleAuthenticationException(AuthenticationException ex) {
        log.error("Inside GlobalExceptionHandler :: handleAuthenticationException : {}", ex.getMessage());
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponseDto);
    }

    @ExceptionHandler(ActionNotAllowedException.class)
    public ResponseEntity<ApiResponseDto> handleActionNotAllowedException(ActionNotAllowedException ex) {
        log.error("Inside GlobalExceptionHandler :: handleActionNotAllowedException : {}", ex.getMessage());
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.CONFLICT.value(), ex.getMessage());
        return ResponseEntity.status(apiResponseDto.getStatus()).body(apiResponseDto);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Inside GlobalExceptionHandler :: handleResourceNotFoundException :", ex);

        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(apiResponseDto.getStatus()).body(apiResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Inside GlobalExceptionHandler :: handleValidationException :", ex);
        FieldError fieldError = ex.getBindingResult().getFieldErrors().getFirst();
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), fieldError.getDefaultMessage());
        return ResponseEntity.badRequest().body(apiResponseDto);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleUnknownException(Exception ex) {
        log.error("Inside GlobalExceptionHandler :: handleUnknownException :", ex);

        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong!");
        return ResponseEntity.internalServerError().body(apiResponseDto);
    }
}
