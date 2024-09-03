package com.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marketplace.model.Buyer;
import com.marketplace.model.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class CommentDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @NotNull(message = "Buyer ID cannot be null")
    private Long buyerId;

    @NotNull(message = "Product ID cannot be null")
    private Long productId;
}
