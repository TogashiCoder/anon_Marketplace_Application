package com.marketplace.exception;

import com.marketplace.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

// This class handles exceptions thrown by any controller in the application.
// It provides a centralized way to manage error responses.
@ControllerAdvice
public class GlobalExceptionHandler {

    // This method handles ProductNotFoundException.
    // When this exception is thrown, this method will be invoked.
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleProductNotFoundException(
            ProductNotFoundException exception,
            WebRequest webRequest)
    {
        // Create an ErrorResponseDto with details about the error.
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        // Return the error response with HTTP status BAD_REQUEST (400).
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }
}