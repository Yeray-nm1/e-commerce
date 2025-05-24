package com.yeray.ecommerce.cart.service.api;

import com.yeray.ecommerce.cart.model.dtos.CartRequestDto;
import com.yeray.ecommerce.cart.model.dtos.CartResponseDto;

public interface CartService {
    /**
     * Creates a new cart.
     *
     * @return the created cart entity with no products
     */
    CartResponseDto createCart();

    /**
     * Retrieves a cart by its ID.
     *
     * @param id the ID of the cart
     * @return the cart entity with the products info and last activity timestamp
     */
    CartResponseDto getCartById(Long id);

    /**
     * Adds products to a cart.
     *
     * @param id       the ID of the cart
     * @param products the list of products to add
     * @return the updated cart entity
     */
    CartResponseDto addProductsToCart(Long id, CartRequestDto products);

    /**
     * Deletes a cart by its ID.
     *
     * @param id the ID of the cart to delete
     */
    void deleteCart(Long id);
}
