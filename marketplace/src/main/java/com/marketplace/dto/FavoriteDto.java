package com.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class FavoriteDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "Buyer ID cannot be null")
    private Long buyerId;

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime favoritedAt;

}
