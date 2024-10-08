package com.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {

    public RatingDTO(Long id, BigDecimal value, Long productId, String comment, Long sellerId, Long buyerId, LocalDateTime createdAt) {
        this.id = id;
        this.value = value;
        this.productId = productId;
        this.comment = comment;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.createdAt = createdAt;
    }



    private Long id;

    @NotNull(message = "Rating value is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Rating value must be at least 0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating value must not exceed 5")
    private BigDecimal value;

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be a positive number")
    private Long productId;

    @NotNull(message = "Comment is required")
    @Size(min = 5, max = 500, message = "Comment must be between 5 and 1000 characters")
    private String comment;

    @NotNull(message = "Seller ID is required")
    @Positive(message = "Seller ID must be a positive number")
    private Long sellerId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;


    @NotNull(message = "Buyer ID is required")
    @Positive(message = "Buyer ID must be a positive number")
    private Long buyerId;
}