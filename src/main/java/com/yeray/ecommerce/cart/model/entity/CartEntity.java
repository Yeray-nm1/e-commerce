package com.yeray.ecommerce.cart.model.entity;

import com.yeray.ecommerce.product.model.entity.ProductEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Represents a shopping cart with products and last activity date.")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartEntity {
    @Schema(description = "Unique identifier for the cart", example = "12345")
    private Long id;
    @Schema(description = "List of products in the cart")
    private List<ProductEntity> products = new ArrayList<>();
    @Schema(description = "Date of the last activity on the cart")
    private Instant lastActivity;
}
