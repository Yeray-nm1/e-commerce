package com.yeray.ecommerce.cart.controller;

import com.yeray.ecommerce.cart.model.dtos.CartRequestDto;
import com.yeray.ecommerce.cart.model.dtos.CartResponseDto;
import com.yeray.ecommerce.cart.service.api.CartService;
import com.yeray.ecommerce.common.exceptions.EntityNotFoundException;
import com.yeray.ecommerce.common.exceptions.InvalidParamException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;
    @InjectMocks
    private CartController cartController;

    private Long validCartId;
    private Long invalidCartId;
    private Long nonExistentCartId;
    private CartRequestDto products;
    private CartResponseDto expectedResponse;

    @BeforeEach
    void setUp() {
        validCartId = 1L; // Assuming this ID exists
        invalidCartId = -1L; // Assuming negative ID is invalid
        nonExistentCartId = 999L; // Assuming this ID does not exist
        products = new CartRequestDto(); // Assuming this is a valid request
        expectedResponse = new CartResponseDto(validCartId, new ArrayList<>(), Instant.now());
    }

    @Test
    @DisplayName("Create Cart - Internal Server Error")
    void createCart_internalServerError() {
        // Arrange
        Mockito.when(cartService.createCart()).thenThrow(new RuntimeException("Internal server error"));
        // Act
        ResponseEntity<Object> response = cartController.createCart();
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Create Cart - Success")
    void createCart_ok() {
        // Arrange
        Mockito.when(cartService.createCart()).thenReturn(expectedResponse);
        // Act
        ResponseEntity<Object> response = cartController.createCart();
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(CartResponseDto.class, response.getBody());
    }

    @Test
    @DisplayName("Get Cart - Bad Request")
    void getCart_badRequest() {
        // Arrange
        Mockito.when(cartService.getCartById(invalidCartId))
                .thenThrow(new InvalidParamException("Invalid cart ID format: " + invalidCartId));
        // Act
        ResponseEntity<Object> response = cartController.getCart(invalidCartId);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Get Cart - Cart Not Found")
    void getCart_cartNotFound() {
        // Arrange
        Mockito.when(cartService.getCartById(nonExistentCartId))
                .thenThrow(new EntityNotFoundException("Cart not found with ID: " + nonExistentCartId));
        // Act
        ResponseEntity<Object> response = cartController.getCart(nonExistentCartId);
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Get Cart - Internal Server Error")
    void getCart_internalServerError() {
        // Arrange
        Mockito.when(cartService.getCartById(validCartId))
                .thenThrow(new RuntimeException("Internal server error"));
        // Act
        ResponseEntity<Object> response = cartController.getCart(validCartId);
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Get Cart - Success")
    void getCart_ok() {
        // Arrange
        Mockito.when(cartService.getCartById(validCartId)).thenReturn(expectedResponse);
        // Act
        ResponseEntity<Object> response = cartController.getCart(validCartId);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(CartResponseDto.class, response.getBody());
        CartResponseDto actualResponse = (CartResponseDto) response.getBody();
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getProducts(), actualResponse.getProducts());
        assertEquals(expectedResponse.getLastActivity(), actualResponse.getLastActivity());
    }

    @Test
    @DisplayName("Add Product to Cart - Internal Server Error")
    void addProductToCart_internalServerError() {
        // Arrange
        Mockito.when(cartService.addProductsToCart(Mockito.eq(validCartId), Mockito.any()))
                .thenThrow(new RuntimeException("Internal server error"));
        // Act
        ResponseEntity<Object> response = cartController.addProductToCart(validCartId, new CartRequestDto());
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Add Product to Cart - Bad Request")
    void addProductToCart_badRequest() {
        // Arrange
        Mockito.when(cartService.addProductsToCart(Mockito.eq(invalidCartId), Mockito.any()))
                .thenThrow(new InvalidParamException("Invalid input data: " + invalidCartId));
        // Act
        ResponseEntity<Object> response = cartController.addProductToCart(invalidCartId, new CartRequestDto());
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Add Product to Cart - Cart Not Found")
    void addProductToCart_cartNotFound() {
        // Arrange
        Mockito.when(cartService.addProductsToCart(Mockito.eq(nonExistentCartId), Mockito.any()))
                .thenThrow(new EntityNotFoundException("Cart not found with ID: " + nonExistentCartId));
        // Act
        ResponseEntity<Object> response = cartController.addProductToCart(nonExistentCartId, new CartRequestDto());
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Add Product to Cart - Success")
    void addProductToCart_ok() {
        // Arrange
        Mockito.when(cartService.addProductsToCart(Mockito.eq(validCartId), Mockito.any(CartRequestDto.class)))
                .thenReturn(expectedResponse);
        // Act
        ResponseEntity<Object> response = cartController.addProductToCart(validCartId, products);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(CartResponseDto.class, response.getBody());
        CartResponseDto actualResponse = (CartResponseDto) response.getBody();
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getProducts(), actualResponse.getProducts());
        assertEquals(expectedResponse.getLastActivity(), actualResponse.getLastActivity());
    }

    @Test
    @DisplayName("Delete Cart - Internal Server Error")
    void deleteCart_internalServerError() {
        // Arrange
        Mockito.doThrow(new RuntimeException("Internal server error"))
                .when(cartService).deleteCart(validCartId);
        // Act
        ResponseEntity<Object> response = cartController.deleteCart(validCartId);
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Delete Cart - Bad Request")
    void deleteCart_badRequest() {
        // Arrange
        Mockito.doThrow(new InvalidParamException("Invalid cart ID format: " + invalidCartId))
                .when(cartService).deleteCart(invalidCartId);
        // Act
        ResponseEntity<Object> response = cartController.deleteCart(invalidCartId);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Delete Cart - Cart Not Found")
    void deleteCart_cartNotFound() {
        // Arrange
        Mockito.doThrow(new EntityNotFoundException("Cart not found with ID: " + nonExistentCartId))
                .when(cartService).deleteCart(nonExistentCartId);
        // Act
        ResponseEntity<Object> response = cartController.deleteCart(nonExistentCartId);
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Delete Cart - Success")
    void deleteCart_ok() {
        // Arrange
        Mockito.doNothing().when(cartService).deleteCart(validCartId);
        // Act
        ResponseEntity<Object> response = cartController.deleteCart(validCartId);
        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}