package com.yeray.ecommerce.cart.utils;

import com.yeray.ecommerce.cart.model.dtos.CartRequestDto;
import com.yeray.ecommerce.common.exceptions.InvalidParamException;
import com.yeray.ecommerce.common.utils.ValidationUtils;

public class CartValidations {

    private CartValidations() {
        // Private constructor to prevent instantiation
    }

    public static void validateCartRequest(CartRequestDto cartRequestDto) {
        if (cartRequestDto == null || cartRequestDto.getProducts() == null || cartRequestDto.getProducts().isEmpty()) {
            throw new InvalidParamException("Cart request must contain at least one product");
        }

        cartRequestDto.getProducts().forEach(product -> {
            ValidationUtils.validateId(product.getId(), "Product ID");
            ValidationUtils.validateParam(product.getDescription(), "Product description");
            ValidationUtils.validateProductAmount(product.getAmount());
        });
    }
}
