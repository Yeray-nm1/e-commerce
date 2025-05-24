package com.yeray.ecommerce.cart.controller;

import com.yeray.ecommerce.cart.model.dtos.CartRequestDto;
import com.yeray.ecommerce.cart.model.dtos.CartResponseDto;
import com.yeray.ecommerce.cart.service.api.CartService;
import com.yeray.ecommerce.common.exceptions.EntityNotFoundException;
import com.yeray.ecommerce.common.exceptions.InvalidParamException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.yeray.ecommerce.common.config.ErrorResponse;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Create a new cart", description = "Creates a new shopping cart and returns its details.")
    @ApiResponse(responseCode = "200", description = "Cart created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponseDto.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/create")
    public ResponseEntity<Object> createCart() {
        try {
            CartResponseDto cart = cartService.createCart();
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred while trying to create the cart: " + e.getMessage()));
        }
    }

    @Operation(summary = "Get cart by ID", description = "Retrieves the details of a cart by its ID.")
    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid cart ID format", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Cart not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCart(@Parameter(description = "cart id") @PathVariable Long id) {
        try {
            CartResponseDto cart = cartService.getCartById(id);
            return ResponseEntity.ok(cart);
        } catch (InvalidParamException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Invalid cart ID format: " + e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred while trying to retrieve the cart: " + e.getMessage()));
        }
    }

    @Operation(summary = "Add products to cart", description = "Adds a list of products to the specified cart.")
    @ApiResponse(responseCode = "200", description = "Products added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid product data or cart ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Cart not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{id}/products")
    public ResponseEntity<Object> addProductToCart(@Parameter(description = "cart id") @PathVariable Long id,
                                                       @RequestBody CartRequestDto products) {
        try {
            CartResponseDto updatedCart = cartService.addProductsToCart(id, products);
            return ResponseEntity.ok(updatedCart);
        } catch (InvalidParamException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Invalid input data: " + e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred while trying to add products to the cart: " + e.getMessage()));
        }
    }

    @Operation(summary = "Delete cart", description = "Deletes a cart and all the products of the cart by its ID.")
    @ApiResponse(responseCode = "204", description = "Cart deleted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid cart ID format", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Cart not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCart(@Parameter(description = "cart id") @PathVariable Long id) {
        try {
            cartService.deleteCart(id);
            return ResponseEntity.noContent().build();
        } catch (InvalidParamException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Invalid cart ID format: " + e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred while trying to delete the cart: " + e.getMessage()));
        }
    }
}
