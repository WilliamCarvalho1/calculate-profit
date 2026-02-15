package com.studies.calculateprofit.adapter.in.controller;

import com.studies.calculateprofit.adapter.in.controller.error.ApiErrorResponse;
import com.studies.calculateprofit.adapter.in.controller.error.ErrorCode;
import com.studies.calculateprofit.application.exception.DomainRuleViolationException;
import com.studies.calculateprofit.application.exception.InvalidRequestException;
import com.studies.calculateprofit.application.exception.RequestNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleNotFoundReturns404() {
        RequestNotFoundException ex = new RequestNotFoundException(1L);
        ResponseEntity<ApiErrorResponse> response = handler.handleNotFound(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorCode.REQUEST_NOT_FOUND, response.getBody().getCode());
        assertNotNull(response.getBody().getCorrelationId());
    }

    @Test
    void handleInvalidRequestDbErrorUsesDbErrorCode() {
        InvalidRequestException ex = new InvalidRequestException("Database error: boom");
        ResponseEntity<ApiErrorResponse> response = handler.handleInvalidRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorCode.DB_ERROR, response.getBody().getCode());
    }

    @Test
    void handleDomainRuleViolationUsesDomainRuleViolationCode() {
        DomainRuleViolationException ex = new DomainRuleViolationException("Income must be positive");
        ResponseEntity<ApiErrorResponse> response = handler.handleDomainRuleViolation(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorCode.DOMAIN_RULE_VIOLATION, response.getBody().getCode());
    }
}
