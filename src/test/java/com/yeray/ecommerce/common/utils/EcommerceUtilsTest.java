package com.yeray.ecommerce.common.utils;

import com.yeray.ecommerce.cart.model.entity.CartEntity;
import com.yeray.ecommerce.product.model.dtos.ProductDto;
import com.yeray.ecommerce.product.model.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EcommerceUtilsTest {

    private EcommerceMapper ecommerceMapper;
    private EcommerceUtils ecommerceUtils;

    @BeforeEach
    void setUp() {
        ecommerceMapper = Mockito.mock(EcommerceMapper.class);
        ecommerceUtils = new EcommerceUtils(ecommerceMapper);
    }

    @Test
    void addOrUpdateProducts_addsAndUpdatesCorrectly() {
        // Productos ya en el carrito
        ProductEntity existing = new ProductEntity(1L, "Prod1", 2);
        List<ProductEntity> cartProducts = new ArrayList<>(List.of(existing));

        // Productos a añadir (uno existente, uno nuevo)
        ProductDto dtoExisting = new ProductDto(1L, "Prod1", 3);
        ProductDto dtoNew = new ProductDto(2L, "Prod2", 1);
        List<ProductDto> productsToAdd = List.of(dtoExisting, dtoNew);

        // Mock del mapeo
        ProductEntity entityExisting = new ProductEntity(1L, "Prod1", 3);
        ProductEntity entityNew = new ProductEntity(2L, "Prod2", 1);
        Mockito.when(ecommerceMapper.mapToEntity(productsToAdd))
                .thenReturn(List.of(entityExisting, entityNew));

        List<ProductEntity> result = ecommerceUtils.addOrUpdateProducts(cartProducts, productsToAdd);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getId() == 1L && p.getAmount() == 5));
        assertTrue(result.stream().anyMatch(p -> p.getId() == 2L && p.getAmount() == 1));
    }

    @Test
    void removeInactiveCarts_removesCorrectly() {
        Instant now = Instant.now();
        CartEntity active = CartEntity.builder().id(1L).lastActivity(now).build();
        CartEntity inactive = CartEntity.builder().id(2L).lastActivity(now.minusSeconds(3600)).build();

        Map<Long, CartEntity> cartStorage = new HashMap<>();
        cartStorage.put(1L, active);
        cartStorage.put(2L, inactive);

        int removed = ecommerceUtils.removeInactiveCarts(cartStorage, 30); // 30 minutos

        assertEquals(1, removed);
        assertFalse(cartStorage.containsKey(2L));
        assertTrue(cartStorage.containsKey(1L));
    }

    @Test
    void removeInactiveCarts_noneToRemove() {
        Instant now = Instant.now();
        CartEntity active1 = CartEntity.builder().id(1L).lastActivity(now).build();
        CartEntity active2 = CartEntity.builder().id(2L).lastActivity(now.minusSeconds(60)).build();

        Map<Long, CartEntity> cartStorage = new HashMap<>();
        cartStorage.put(1L, active1);
        cartStorage.put(2L, active2);

        int removed = ecommerceUtils.removeInactiveCarts(cartStorage, 30); // 30 minutos

        assertEquals(0, removed);
        assertEquals(2, cartStorage.size());
    }
}