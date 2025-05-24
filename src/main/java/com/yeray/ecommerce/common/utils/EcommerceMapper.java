package com.yeray.ecommerce.common.utils;

import com.yeray.ecommerce.product.model.dtos.ProductDto;
import com.yeray.ecommerce.product.model.entity.ProductEntity;
import com.yeray.ecommerce.product.utils.ProductMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EcommerceMapper {

    private final ProductMapper productMapper;

    public EcommerceMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public List<ProductEntity> mapToEntity(List<ProductDto> productDtos) {
        return productDtos.stream()
                .map(productMapper::mapToEntity)
                .toList();
    }

    public List<ProductDto> mapToDto(List<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(productMapper::mapToDto)
                .toList();
    }
}
