package com.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {
    private Long id;

    @NotNull(message = "Rating value is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Rating value must be at least 0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating value must not exceed 5")
    private BigDecimal value;

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be a positive number")
    private Long productId;

    @NotNull(message = "Seller ID is required")
    @Positive(message = "Seller ID must be a positive number")
    private Long sellerId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;
}