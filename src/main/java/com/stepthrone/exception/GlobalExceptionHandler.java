package com.stepthrone.exception;

import com.stepthrone.exception.customexception.ApiError;
import com.stepthrone.exception.customexception.ProfileNotFoundException;
import com.stepthrone.exception.customexception.UnauthorizedUserException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<ApiError> handleUnauthorizedUser(UnauthorizedUserException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI(), "Unauthorized User");
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<ApiError> handleProfileNotFound(ProfileNotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), "Profile Not Found");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());
        String errorMsg = String.join(", ", errorMessages);
        return buildError(HttpStatus.BAD_REQUEST, errorMsg, request.getRequestURI(), "Validation Error");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI(), "Server Error");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String path = ((ServletWebRequest)request).getRequest().getRequestURI();

        if (ex.getRequiredType() == LocalDate.class) {
            return buildError(
                    HttpStatus.BAD_REQUEST,
                    "Invalid date format",
                    path,
                    "Date parameter must be in yyyy-MM-dd format"
            );
        }

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Invalid request parameter",
                path,
                ex.getMessage()
        );
    }
    private ResponseEntity<ApiError> buildError(HttpStatus status, String message, String path, String error) {
        ApiError apiError = new ApiError(
                message,
                path,
                status.value(),
                Instant.now(),
                error
        );
        return ResponseEntity.status(status).body(apiError);
    }
}