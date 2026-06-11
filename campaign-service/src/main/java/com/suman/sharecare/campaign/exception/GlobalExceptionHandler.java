package com.suman.sharecare.campaign.exception;

import com.suman.sharecare.campaign.dto.PageDtos.ApiResponseDto;
import com.suman.sharecare.campaign.dto.ErrorDtos.ValidationErrorResponseDto;
import com.suman.sharecare.campaign.exception.custom_exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Inside GlobalExceptionHandler :: handleResourceNotFoundException : ", ex);

        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(apiResponseDto.getStatus()).body(apiResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Inside GlobalExceptionHandler :: handleValidationException : ", ex);

        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String errorMsg = err.getDefaultMessage();
            errorMap.put(fieldName, errorMsg);
        });
        ValidationErrorResponseDto errorResponseDto = new ValidationErrorResponseDto(HttpStatus.BAD_REQUEST.value(), errorMap);
        return ResponseEntity.badRequest().body(errorResponseDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleUnknownException(Exception ex) {
        log.error("Inside GlobalExceptionHandler :: handleUnknownException : ", ex);

        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong!");
        return ResponseEntity.internalServerError().body(apiResponseDto);
    }
}
