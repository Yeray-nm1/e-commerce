package com.yeray.ecommerce.cart.utils;

import com.yeray.ecommerce.cart.model.dtos.CartResponseDto;
import com.yeray.ecommerce.cart.model.entity.CartEntity;
import com.yeray.ecommerce.common.utils.EcommerceMapper;
import com.yeray.ecommerce.product.model.dtos.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartMapper {

    private final EcommerceMapper ecommerceMapper;

    public CartResponseDto mapToDto(CartEntity cart) {
        if (cart == null) {
            return null;
        }

        List<ProductDto> products = ecommerceMapper.mapToDto(cart.getProducts());
        return CartResponseDto.builder()
                .id(cart.getId())
                .products(products)
                .lastActivity(cart.getLastActivity())
                .build();
    }
}
