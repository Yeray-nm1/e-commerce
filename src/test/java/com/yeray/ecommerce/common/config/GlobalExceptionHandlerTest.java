package com.yeray.ecommerce.common.config;

import com.yeray.ecommerce.common.exceptions.EntityNotFoundException;
import com.yeray.ecommerce.common.exceptions.InvalidParamException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleTypeMismatch_returnsBadRequest() {
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "abc", String.class, "id", null, new IllegalArgumentException("error"));
        var response = handler.handleTypeMismatch(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody());
        assertThat(response.getBody().getMessage()).contains("Invalid value for parameter");
    }

    @Test
    void handleConstraintViolation_returnsBadRequest() {
        ConstraintViolationException ex = new ConstraintViolationException("must not be null", null);
        var response = handler.handleConstraintViolation(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody());
        assertThat(response.getBody().getMessage()).contains("Validation error");
    }

    @Test
    void handleInvalidParam_returnsBadRequest() {
        InvalidParamException ex = new InvalidParamException("Parámetro inválido");
        var response = handler.handleInvalidParam(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody());
        assertThat(response.getBody().getMessage()).isEqualTo("Parámetro inválido");
    }

    @Test
    void handleNotFound_returnsNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("No encontrado");
        var response = handler.handleNotFound(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertNotNull(response.getBody());
        assertThat(response.getBody().getMessage()).isEqualTo("No encontrado");
    }

}