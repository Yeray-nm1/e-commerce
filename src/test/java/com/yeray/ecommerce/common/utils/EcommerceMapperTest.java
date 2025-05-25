package com.yeray.ecommerce.common.utils;

import com.yeray.ecommerce.product.model.dtos.ProductDto;
import com.yeray.ecommerce.product.model.entity.ProductEntity;
import com.yeray.ecommerce.product.utils.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EcommerceMapperTest {

    private ProductMapper productMapper;
    private EcommerceMapper ecommerceMapper;

    @BeforeEach
    void setUp() {
        productMapper = Mockito.mock(ProductMapper.class);
        ecommerceMapper = new EcommerceMapper(productMapper);
    }

    @Test
    void mapToEntity() {
        ProductDto dto = new ProductDto(1L, "Producto", 2);
        ProductEntity entity = new ProductEntity(1L, "Producto", 2);
        Mockito.when(productMapper.mapToEntity(dto)).thenReturn(entity);

        List<ProductEntity> result = ecommerceMapper.mapToEntity(List.of(dto));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(entity, result.get(0));
    }

    @Test
    void mapToDto() {
        ProductEntity entity = new ProductEntity(1L, "Producto", 2);
        ProductDto dto = new ProductDto(1L, "Producto", 2);
        Mockito.when(productMapper.mapToDto(entity)).thenReturn(dto);

        List<ProductDto> result = ecommerceMapper.mapToDto(List.of(entity));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }
}