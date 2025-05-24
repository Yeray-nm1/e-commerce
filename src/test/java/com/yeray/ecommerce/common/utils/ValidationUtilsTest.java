package com.yeray.ecommerce.common.utils;

import com.yeray.ecommerce.common.exceptions.InvalidParamException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    @Test
    @DisplayName("validateId with valid IDs should not throw an exception")
    void validateId_validId_noException() {
        assertDoesNotThrow(() -> ValidationUtils.validateId(1L, "id"));
        assertDoesNotThrow(() -> ValidationUtils.validateId(100L, "id"));
    }

    @Test
    @DisplayName("validateId with null or negative IDs should throw InvalidParamException")
    void validateId_nullOrNegative_throwsException() {
        assertThrows(InvalidParamException.class, () -> ValidationUtils.validateId(null, "id"));
        assertThrows(InvalidParamException.class, () -> ValidationUtils.validateId(0L, "id"));
        assertThrows(InvalidParamException.class, () -> ValidationUtils.validateId(-5L, "id"));
    }

    @Test
    @DisplayName("validateParam with valid values should not throw an exception")
    void validateParam_validValue_noException() {
        assertDoesNotThrow(() -> ValidationUtils.validateParam("valor", "param"));
        assertDoesNotThrow(() -> ValidationUtils.validateParam("  texto  ", "param"));
    }

    @Test
    @DisplayName("validateParam with null or blank values should throw InvalidParamException")
    void validateParam_nullOrBlank_throwsException() {
        assertThrows(InvalidParamException.class, () -> ValidationUtils.validateParam(null, "param"));
        assertThrows(InvalidParamException.class, () -> ValidationUtils.validateParam("", "param"));
        assertThrows(InvalidParamException.class, () -> ValidationUtils.validateParam("   ", "param"));
    }

    @Test
    @DisplayName("validateProductAmount with valid amounts should not throw an exception")
    void validateProductAmount_valid_noException() {
        assertDoesNotThrow(() -> ValidationUtils.validateProductAmount(0));
        assertDoesNotThrow(() -> ValidationUtils.validateProductAmount(10));
    }

    @Test
    @DisplayName("validateProductAmount with null or negative amounts should throw InvalidParamException")
    void validateProductAmount_nullOrNegative_throwsException() {
        assertThrows(InvalidParamException.class, () -> ValidationUtils.validateProductAmount(null));
        assertThrows(InvalidParamException.class, () -> ValidationUtils.validateProductAmount(-1));
    }
}