package com.yeray.ecommerce.common.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Schema(description = "Error response model")
public class ErrorResponse {
    @Schema(description = "Error code")
    private int status;

    @Schema(description = "Error message")
    private String message;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }
}
