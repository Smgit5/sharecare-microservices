package com.suman.sharecare.donation.exception;

import com.razorpay.RazorpayException;
import com.suman.sharecare.donation.dto.page_dtos.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RazorpayException.class)
    public ResponseEntity<ApiResponseDto> handleRazorpayException(RazorpayException ex) {
        log.error("Inside GlobalExceptionHandler :: handleRazorpayException, error = ", ex);
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.BAD_GATEWAY.value(), "Payment gateway experiencing issue. Donation could not be completed.");
        return ResponseEntity.status(apiResponseDto.getStatus()).body(apiResponseDto);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Inside GlobalExceptionHandler :: handleResourceNotFoundException, error = {}", ex.getMessage());
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(apiResponseDto.getStatus()).body(apiResponseDto);
    }

    @ExceptionHandler(ActionNotAllowedException.class)
    public ResponseEntity<ApiResponseDto> handleActionNotAllowedException(ActionNotAllowedException ex) {
        log.error("Inside GlobalExceptionHandler :: handleActionNotAllowedException, error = {}", ex.getMessage());
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.CONFLICT.value(), ex.getMessage());
        return ResponseEntity.status(apiResponseDto.getStatus()).body(apiResponseDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleUnknownException(Exception ex) {
        log.error("Inside GlobalExceptionHandler :: handleUnknownException, error = {}", ex.getMessage());
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong!");
        return ResponseEntity.internalServerError().body(apiResponseDto);
    }
}
