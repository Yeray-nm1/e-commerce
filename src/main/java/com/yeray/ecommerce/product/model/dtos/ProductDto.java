package com.yeray.ecommerce.product.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDto {
    @Schema(description = "Unique identifier for the product", example = "1")
    private Long id;
    @Schema(description = "Product description", example = "Blue shirt")
    private String description;
    @Schema(description = "Amount of product units", example = "2")
    private Integer amount;
}
