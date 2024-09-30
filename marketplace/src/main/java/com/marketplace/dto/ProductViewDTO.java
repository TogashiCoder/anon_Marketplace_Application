package com.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductViewDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private Long productId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime viewedAt;
}
