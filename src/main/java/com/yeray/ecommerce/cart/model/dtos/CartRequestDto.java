package com.yeray.ecommerce.cart.model.dtos;

import com.yeray.ecommerce.product.model.dtos.ProductDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CartRequestDto {
    @Schema(description = "List of products to be added/updated to the cart")
    private List<ProductDto> products;
}
