package com.yeray.ecommerce.cart.utils;

import com.yeray.ecommerce.cart.model.dtos.CartResponseDto;
import com.yeray.ecommerce.cart.model.entity.CartEntity;
import com.yeray.ecommerce.common.utils.EcommerceMapper;
import com.yeray.ecommerce.product.model.dtos.ProductDto;
import com.yeray.ecommerce.product.model.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartMapperTest {

    private EcommerceMapper ecommerceMapper;
    private CartMapper cartMapper;

    @BeforeEach
    void setUp() {
        ecommerceMapper = Mockito.mock(EcommerceMapper.class);
        cartMapper = new CartMapper(ecommerceMapper);
    }

    @Test
    void mapToDto_shouldMapCorrectly() {
        // Arrange
        ProductEntity productEntity = new ProductEntity(1L, "Producto", 2);
        CartEntity cartEntity = CartEntity.builder()
                .id(10L)
                .products(List.of(productEntity))
                .lastActivity(Instant.now())
                .build();

        ProductDto productDto = new ProductDto(1L, "Producto", 2);
        Mockito.when(ecommerceMapper.mapToDto(cartEntity.getProducts()))
                .thenReturn(List.of(productDto));

        // Act
        CartResponseDto dto = cartMapper.mapToDto(cartEntity);

        // Assert
        assertNotNull(dto);
        assertEquals(cartEntity.getId(), dto.getId());
        assertEquals(cartEntity.getLastActivity(), dto.getLastActivity());
        assertEquals(1, dto.getProducts().size());
        assertEquals(productDto, dto.getProducts().get(0));
    }

    @Test
    void mapToDto_nullCart_returnsNull() {
        // Act
        CartResponseDto dto = cartMapper.mapToDto(null);

        // Assert
        assertNull(dto);
    }
}