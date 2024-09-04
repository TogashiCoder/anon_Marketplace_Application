package com.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
public class ProductDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "minimumOrderQuantity is required")
    @Positive(message = "minimumOrderQuantity must be positive")
    private Integer minimumOrderQuantity;


    @NotNull(message = "Seller ID is required")
    private Long sellerId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> imageUrls;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> videoUrls;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}