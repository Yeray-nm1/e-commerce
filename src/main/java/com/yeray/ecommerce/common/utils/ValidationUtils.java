package com.yeray.ecommerce.common.utils;

import com.yeray.ecommerce.common.exceptions.InvalidParamException;

public class ValidationUtils {
    
    private ValidationUtils() {
        // Private constructor to prevent instantiation
    }

    public static void validateId(Long id, String paramName) {
        if (id == null || id <= 0) {
            throw new InvalidParamException("Parameter '" + paramName + "' must be a positive integer.");
        }
    }

    public static void validateParam(String value, String paramName) {
        if (value == null || value.isBlank()) {
            throw new InvalidParamException("Parameter '" + paramName + "' must not be null or empty.");
        }
    }

    public static void validateProductAmount(Integer amount) {
        if (amount == null || amount < 0) {
            throw new InvalidParamException("Product amount must be a non-negative integer");
        }
    }
}
