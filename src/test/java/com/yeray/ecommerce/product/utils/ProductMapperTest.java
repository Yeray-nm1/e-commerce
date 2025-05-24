package com.yeray.ecommerce.product.utils;

import com.yeray.ecommerce.product.model.dtos.ProductDto;
import com.yeray.ecommerce.product.model.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
    }

    @Test
    void mapToEntity_mapsCorrectly() {
        ProductDto dto = new ProductDto(1L, "Producto", 5);
        ProductEntity entity = productMapper.mapToEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getAmount(), entity.getAmount());
    }

    @Test
    void mapToEntity_null_returnsNull() {
        assertNull(productMapper.mapToEntity(null));
    }

    @Test
    void mapToDto_mapsCorrectly() {
        ProductEntity entity = ProductEntity.builder()
                .id(2L)
                .description("Otro producto")
                .amount(3)
                .build();
        ProductDto dto = productMapper.mapToDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getDescription(), dto.getDescription());
        assertEquals(entity.getAmount(), dto.getAmount());
    }

    @Test
    void mapToDto_null_returnsNull() {
        assertNull(productMapper.mapToDto(null));
    }
}