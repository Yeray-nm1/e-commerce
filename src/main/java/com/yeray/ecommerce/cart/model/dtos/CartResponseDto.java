package com.yeray.ecommerce.cart.model.dtos;

import com.yeray.ecommerce.product.model.dtos.ProductDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDto {
    @Schema(description = "Unique identifier for the cart", example = "12345")
    private Long id;
    @Schema(description = "List of products in the cart")
    private List<ProductDto> products;
    @Schema(description = "Date of the last activity on the cart")
    private Instant lastActivity;
}
