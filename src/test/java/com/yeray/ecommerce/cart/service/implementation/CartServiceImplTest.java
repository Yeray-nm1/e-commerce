package com.yeray.ecommerce.cart.service.implementation;

import com.yeray.ecommerce.cart.model.dtos.CartRequestDto;
import com.yeray.ecommerce.cart.model.dtos.CartResponseDto;
import com.yeray.ecommerce.cart.utils.CartMapper;
import com.yeray.ecommerce.common.config.CartConfig;
import com.yeray.ecommerce.common.exceptions.EntityNotFoundException;
import com.yeray.ecommerce.common.exceptions.InvalidParamException;
import com.yeray.ecommerce.common.utils.EcommerceUtils;
import com.yeray.ecommerce.product.model.dtos.ProductDto;
import com.yeray.ecommerce.product.model.entity.ProductEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartConfig cartConfig;
    @Mock
    private CartMapper cartMapper;
    @Mock
    private EcommerceUtils ecommerceUtils;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    @DisplayName("Create Cart - Should create a new cart successfully")
    void createCart_ok() {
        // Arrange
        CartResponseDto expectedCart = new CartResponseDto(1L, new ArrayList<>(), Instant.now());
        Mockito.when(cartMapper.mapToDto(Mockito.any())).thenReturn(expectedCart);
        // Act
        CartResponseDto actualCart = cartService.createCart();
        // Assert
        assertNotNull(actualCart);
        assertEquals(expectedCart.getId(), actualCart.getId());
        assertTrue(actualCart.getProducts().isEmpty());
    }

    @Test
    @DisplayName("Get Cart by ID - Invalid ID format")
    void getCartById_invalidId() {
        // Arrange
        Long cartId = -1L;
        // Act & Assert
        assertThrows(InvalidParamException.class, () -> cartService.getCartById(cartId));
    }

    @Test
    @DisplayName("Get Cart by ID - Cart not found")
    void getCartById_cartNotFound() {
        // Arrange
        Long cartId = 1L;
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> cartService.getCartById(cartId));
    }

    @Test
    @DisplayName("Get Cart by ID - Should return cart successfully")
    void getCartById_ok() {
        // Arrange
        CartResponseDto expectedCart = new CartResponseDto(1L, new ArrayList<>(), Instant.now());
        Mockito.when(cartMapper.mapToDto(Mockito.any())).thenReturn(expectedCart);
        cartService.createCart();
        // Act
        CartResponseDto actualCart = cartService.getCartById(1L);
        // Assert
        assertNotNull(actualCart);
        assertEquals(expectedCart.getId(), actualCart.getId());
        assertTrue(actualCart.getProducts().isEmpty());
    }

    @Test
    @DisplayName("Add Products to Cart - Invalid ID format")
    void addProductsToCart_invalidId() {
        // Arrange
        Long cartId = -1L;
        // Act & Assert
        assertThrows(InvalidParamException.class, () -> cartService.addProductsToCart(cartId, null));
    }

    @Test
    @DisplayName("Add Products to Cart - Invalid product")
    void addProductsToCart_invalidProduct() {
        // Arrange
        Long cartId = 1L;
        Long invalidProductId = -1L;
        // empty cart
        CartRequestDto cartEmpty = new CartRequestDto();
        // cart with empty product list
        CartRequestDto cartWithEmptyProducts = new CartRequestDto();
        cartWithEmptyProducts.setProducts(new ArrayList<>());
        // cart with invalid product description
        CartRequestDto cartWithInvalidDescription = new CartRequestDto();
        ProductDto invalidProduct = new ProductDto(1L, null, 1);
        List<ProductDto> descriptionsNotValid = List.of(invalidProduct);
        cartWithInvalidDescription.setProducts(descriptionsNotValid);
        // cart with invalid product amount
        CartRequestDto cartWithInvalidAmount = new CartRequestDto();
        List<ProductDto> amountsNotValid = new ArrayList<>();
        amountsNotValid.add(new ProductDto(1L, "Valid Description", -1));
        cartWithInvalidAmount.setProducts(amountsNotValid);
        // cart with invalid product ID
        CartRequestDto cartWithInvalidId = new CartRequestDto();
        List<ProductDto> productsWithInvalidId = new ArrayList<>();
        productsWithInvalidId.add(new ProductDto(invalidProductId, "Valid Description", 1));
        cartWithInvalidId.setProducts(productsWithInvalidId);
        // Act & Assert
        assertThrows(InvalidParamException.class, () -> cartService.addProductsToCart(cartId, null));
        assertThrows(InvalidParamException.class, () -> cartService.addProductsToCart(cartId, cartEmpty));
        assertThrows(InvalidParamException.class, () -> cartService.addProductsToCart(cartId, cartWithEmptyProducts));
        assertThrows(InvalidParamException.class, () -> cartService.addProductsToCart(cartId, cartWithInvalidId));
        assertThrows(InvalidParamException.class, () -> cartService.addProductsToCart(cartId, cartWithInvalidDescription));
        assertThrows(InvalidParamException.class, () -> cartService.addProductsToCart(cartId, cartWithInvalidAmount));
    }

    @Test
    @DisplayName("Add Products to Cart - Cart not found")
    void addProductsToCart_cartNotFound() {
        // Arrange
        Long cartId = 2L;
        CartRequestDto cartRequest = new CartRequestDto();
        cartRequest.setProducts(List.of(new ProductDto(1L, "Valid Description", 1)));
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> cartService.addProductsToCart(cartId, cartRequest));
    }

    @Test
    @DisplayName("Add Products to Cart - Should add products successfully")
    void addProductsToCart_ok() {
        // Arrange
        Long cartId = 1L;
        cartService.createCart();
        CartRequestDto cartRequest = new CartRequestDto();
        List<ProductDto> products = new ArrayList<>();
        products.add(new ProductDto(1L, "Product 1", 2));
        cartRequest.setProducts(products);

        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.add(new ProductEntity(1L, "Product 1", 2));
        Mockito.when(ecommerceUtils.addOrUpdateProducts(Mockito.anyList(), Mockito.anyList())).thenReturn(productEntities);

        Mockito.when(cartMapper.mapToDto(Mockito.any())).thenAnswer(invocation -> {
            var cartEntity = (com.yeray.ecommerce.cart.model.entity.CartEntity) invocation.getArgument(0);
            List<ProductDto> productDtos = new ArrayList<>();
            for (var p : cartEntity.getProducts()) {
                productDtos.add(new ProductDto(p.getId(), p.getDescription(), p.getAmount()));
            }
            return new CartResponseDto(cartEntity.getId(), productDtos, cartEntity.getLastActivity());
        });
        // Act
        CartResponseDto updatedCart = cartService.addProductsToCart(cartId, cartRequest);
        // Assert
        assertNotNull(updatedCart);
        assertEquals(cartId, updatedCart.getId());
        assertEquals(1, updatedCart.getProducts().size());
        assertEquals("Product 1", updatedCart.getProducts().getFirst().getDescription());
        assertEquals(2, updatedCart.getProducts().getFirst().getAmount());
    }

    @Test
    @DisplayName("Add Products to Cart - Should update amount of existing products")
    void addProductsToCart_updateAmount() {
        // Arrange
        Long cartId = 1L;
        cartService.createCart();
        CartRequestDto cartRequest = new CartRequestDto();
        List<ProductDto> products = new ArrayList<>();
        products.add(new ProductDto(1L, "Product 1", 2));
        products.add(new ProductDto(1L, "Product 1", 3)); // Same product with different amount
        cartRequest.setProducts(products);

        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.add(new ProductEntity(1L, "Product 1", 5)); // Total amount should be 5
        Mockito.when(ecommerceUtils.addOrUpdateProducts(Mockito.anyList(), Mockito.anyList())).thenReturn(productEntities);

        Mockito.when(cartMapper.mapToDto(Mockito.any())).thenAnswer(invocation -> {
            var cartEntity = (com.yeray.ecommerce.cart.model.entity.CartEntity) invocation.getArgument(0);
            List<ProductDto> productDtos = new ArrayList<>();
            for (var p : cartEntity.getProducts()) {
                productDtos.add(new ProductDto(p.getId(), p.getDescription(), p.getAmount()));
            }
            return new CartResponseDto(cartEntity.getId(), productDtos, cartEntity.getLastActivity());
        });
        // Act
        CartResponseDto updatedCart = cartService.addProductsToCart(cartId, cartRequest);
        // Assert
        assertNotNull(updatedCart);
        assertEquals(cartId, updatedCart.getId());
        assertEquals(1, updatedCart.getProducts().size());
        assertEquals("Product 1", updatedCart.getProducts().getFirst().getDescription());
        assertEquals(5, updatedCart.getProducts().getFirst().getAmount()); // Amount should be updated to 5
    }

    @Test
    @DisplayName("Delete Cart - Invalid ID format")
    void deleteCart_invalidId() {
        // Arrange
        Long cartId = -1L;
        // Act & Assert
        assertThrows(InvalidParamException.class, () -> cartService.deleteCart(cartId));
    }

    @Test
    @DisplayName("Delete Cart - cart not found")
    void deleteCart_cartNotFound() {
        // Arrange
        Long cartId = 1L;
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> cartService.deleteCart(cartId));
    }

    @Test
    @DisplayName("Delete Cart - Should delete cart successfully")
    void deleteCart_ok() {
        // Arrange
        CartResponseDto expectedCart = new CartResponseDto(1L, new ArrayList<>(), Instant.now());
        Mockito.when(cartMapper.mapToDto(Mockito.any())).thenReturn(expectedCart);
        CartResponseDto cart = cartService.createCart();
        Long cartId = cart.getId();
        // Assert
        assertDoesNotThrow(() -> cartService.deleteCart(cartId));
        assertThrows(EntityNotFoundException.class, () -> cartService.getCartById(cartId));
    }

    @Test
    @DisplayName("Remove Inactive Carts - Should remove carts inactive for more than timeout")
    void removeInactiveCarts_ok() {
        // Arrange
        Mockito.when(ecommerceUtils.removeInactiveCarts(Mockito.anyMap(), Mockito.anyInt()))
                .thenReturn(2);
        // Act & Assert
        assertDoesNotThrow(() -> cartService.removeInactiveCarts());
        Mockito.verify(ecommerceUtils, Mockito.times(1))
                .removeInactiveCarts(Mockito.anyMap(), Mockito.anyInt());
    }

    @Test
    @DisplayName("Remove Inactive Carts - Nothing to remove")
    void removeInactiveCarts_noneRemoved() {
        // Arrange
        Mockito.when(ecommerceUtils.removeInactiveCarts(Mockito.anyMap(), Mockito.anyInt()))
                .thenReturn(0);
        // Act & Assert
        assertDoesNotThrow(() -> cartService.removeInactiveCarts());
        Mockito.verify(ecommerceUtils, Mockito.times(1))
                .removeInactiveCarts(Mockito.anyMap(), Mockito.anyInt());
    }
}