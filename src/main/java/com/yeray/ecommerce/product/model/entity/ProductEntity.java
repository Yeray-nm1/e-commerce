package com.yeray.ecommerce.product.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Represents a product which can be added to a cart.")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {
    @Schema(description = "Unique identifier for the product", example = "1")
    private Long id;
    @Schema(description = "Product description", example = "Blue shirt")
    private String description;
    @Schema(description = "Amount of product units", example = "2")
    private Integer amount;
}
