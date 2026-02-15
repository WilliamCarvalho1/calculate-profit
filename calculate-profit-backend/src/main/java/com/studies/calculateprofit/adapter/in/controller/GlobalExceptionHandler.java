package com.studies.calculateprofit.adapter.in.controller;

import com.studies.calculateprofit.adapter.in.controller.error.ApiErrorResponse;
import com.studies.calculateprofit.adapter.in.controller.error.ErrorCode;
import com.studies.calculateprofit.application.exception.DomainRuleViolationException;
import com.studies.calculateprofit.application.exception.InvalidRequestException;
import com.studies.calculateprofit.application.exception.RequestNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(RequestNotFoundException ex) {
        String correlationId = UUID.randomUUID().toString();
        log.info("[{}] Request not found: {}", correlationId, ex.getMessage());
        return build(
                HttpStatus.NOT_FOUND,
                ErrorCode.REQUEST_NOT_FOUND,
                ex,
                correlationId
        );
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRequest(InvalidRequestException ex) {
        String correlationId = UUID.randomUUID().toString();
        log.warn("[{}] Invalid request: {}", correlationId, ex.getMessage());

        ErrorCode code = ex.getMessage() != null && ex.getMessage().startsWith("Database error:")
                ? ErrorCode.DB_ERROR
                : ErrorCode.BAD_REQUEST;

        return build(
                HttpStatus.BAD_REQUEST,
                code,
                ex,
                correlationId
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex) {
        String correlationId = UUID.randomUUID().toString();
        String url = ex.getRequestURL();
        log.info("[{}] No handler found for {} {}", correlationId, ex.getHttpMethod(), url);

        if (url.startsWith("/api/v1/shipments/")) {
            return build(
                    HttpStatus.BAD_REQUEST,
                    ErrorCode.BAD_REQUEST,
                    "ID must not be null.",
                    correlationId
            );
        }
        return build(
                HttpStatus.NOT_FOUND,
                ErrorCode.REQUEST_NOT_FOUND,
                "Resource not found.",
                correlationId
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String correlationId = UUID.randomUUID().toString();
        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));

        String message = details.isBlank() ? "Request validation failed" : details;

        log.warn("[{}] Validation failed: {}", correlationId, message);

        return build(
                HttpStatus.BAD_REQUEST,
                ErrorCode.VALIDATION_ERROR,
                message,
                correlationId
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        String correlationId = UUID.randomUUID().toString();
        log.error("[{}] Unexpected error", correlationId, ex);
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_ERROR,
                "Unexpected error",
                correlationId
        );
    }

    @ExceptionHandler(DomainRuleViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDomainRuleViolation(DomainRuleViolationException ex) {
        String correlationId = UUID.randomUUID().toString();
        log.warn("[{}] Domain rule violation: {}", correlationId, ex.getMessage());
        return build(
                HttpStatus.BAD_REQUEST,
                ErrorCode.DOMAIN_RULE_VIOLATION,
                ex,
                correlationId
        );
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, ErrorCode code, Exception ex, String correlationId) {
        return build(
                status,
                code,
                ex.getMessage(),
                correlationId
        );
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, ErrorCode code, String message, String correlationId) {
        return ResponseEntity
                .status(status)
                .body(
                        new ApiErrorResponse(
                                status.value(),
                                status.getReasonPhrase(),
                                code,
                                message,
                                correlationId
                        )
                );
    }

    private String formatFieldError(FieldError fieldError) {
        return String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage());
    }
}