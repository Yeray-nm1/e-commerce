package com.yeray.ecommerce.product.utils;

import com.yeray.ecommerce.product.model.dtos.ProductDto;
import com.yeray.ecommerce.product.model.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductEntity mapToEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }
        return ProductEntity.builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .build();
    }

    public ProductDto mapToDto(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        return new ProductDto(
                entity.getId(),
                entity.getDescription(),
                entity.getAmount()
        );
    }
}
