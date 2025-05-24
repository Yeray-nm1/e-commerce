package com.yeray.ecommerce.cart.service.implementation;

import com.yeray.ecommerce.cart.model.dtos.CartRequestDto;
import com.yeray.ecommerce.cart.model.dtos.CartResponseDto;
import com.yeray.ecommerce.cart.model.entity.CartEntity;
import com.yeray.ecommerce.cart.service.api.CartService;
import com.yeray.ecommerce.cart.utils.CartMapper;
import com.yeray.ecommerce.cart.utils.CartValidations;
import com.yeray.ecommerce.common.config.CartConfig;
import com.yeray.ecommerce.common.exceptions.EntityNotFoundException;
import com.yeray.ecommerce.common.utils.EcommerceUtils;
import com.yeray.ecommerce.common.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);
    public static final String CART_ID = "Cart ID";
    private final Map<Long, CartEntity> cartStorage = new ConcurrentHashMap<>();
    private final AtomicLong cartIdGenerator = new AtomicLong(1);
    private final CartConfig cartConfig;
    private final CartMapper cartMapper;
    private final EcommerceUtils ecommerceUtils;

    @Override
    public CartResponseDto createCart() {
        long id = cartIdGenerator.getAndIncrement();
        CartEntity cart = CartEntity.builder()
                .id(id)
                .products(new ArrayList<>())
                .lastActivity(Instant.now())
                .build();
        cartStorage.put(id, cart);
        return cartMapper.mapToDto(cart);
    }

    @Override
    public CartResponseDto getCartById(Long id) {
        ValidationUtils.validateId(id, CART_ID);
        CartEntity cart = cartStorage.get(id);
        if (cart == null) {
            throw new EntityNotFoundException("Cart not found with ID: " + id);
        }
        // Update last activity timestamp
        cart.setLastActivity(Instant.now());
        return cartMapper.mapToDto(cart);
    }

    @Override
    public CartResponseDto addProductsToCart(Long id, CartRequestDto cartRequest) {
        ValidationUtils.validateId(id, CART_ID);
        CartValidations.validateCartRequest(cartRequest);

        CartEntity cart = cartStorage.get(id);
        if (cart == null) {
            throw new EntityNotFoundException("Cart not found with ID: " + id);
        }
        cart.setProducts(ecommerceUtils.addOrUpdateProducts(cart.getProducts(), cartRequest.getProducts()));
        cart.setLastActivity(Instant.now());
        return cartMapper.mapToDto(cart);
    }

    @Override
    public void deleteCart(Long id) {
        ValidationUtils.validateId(id, CART_ID);
        if (!cartStorage.containsKey(id)) {
            throw new EntityNotFoundException("Cart not found with ID: " + id);
        }
        cartStorage.remove(id);
    }

    @Scheduled(fixedRate = 60_000)
    public void removeInactiveCarts() {
        int removedCount = ecommerceUtils.removeInactiveCarts(cartStorage, cartConfig.getInactiveTimeoutMinutes());
        if (removedCount > 0) {
            log.info("Removed {} inactive carts", removedCount);
        }
    }
}
