package com.yeray.ecommerce.common.utils;

import com.yeray.ecommerce.product.model.dtos.ProductDto;
import com.yeray.ecommerce.cart.model.entity.CartEntity;
import com.yeray.ecommerce.product.model.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EcommerceUtils {

    private final EcommerceMapper ecommerceMapper;

    public EcommerceUtils(EcommerceMapper ecommerceMapper) {
        this.ecommerceMapper = ecommerceMapper;
    }

    public List<ProductEntity> addOrUpdateProducts(List<ProductEntity> cartProducts, List<ProductDto> productsToAdd) {
        Map<Long, ProductEntity> cartMap = new HashMap<>();
        for (ProductEntity product : cartProducts) {
            cartMap.put(product.getId(), product);
        }
        List<ProductEntity> productsToAddEntities = ecommerceMapper.mapToEntity(productsToAdd);
        for (ProductEntity newProduct : productsToAddEntities) {
            ProductEntity existing = cartMap.get(newProduct.getId());
            if (existing != null) {
                // Update existing product
                existing.setAmount(existing.getAmount() + newProduct.getAmount());
            } else {
                // Add new product
                cartProducts.add(newProduct);
                cartMap.put(newProduct.getId(), newProduct);
            }
        }
        return cartProducts;
    }

    public int removeInactiveCarts(Map<Long, CartEntity> cartStorage, int minutesInactive) {
        Instant now = Instant.now();
        List<Long> toRemove = cartStorage.entrySet().stream()
                .filter(cart -> Duration.between(cart.getValue().getLastActivity(), now).toMinutes() >= minutesInactive)
                .map(Map.Entry::getKey)
                .toList();
        toRemove.forEach(cartStorage::remove);
        return toRemove.size();
    }

}
