package com.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponUsageDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotNull(message = "Coupon ID cannot be null")
    private Long couponId;
    @NotNull(message = "Buyer ID cannot be null")
    private Long buyerId;
    @NotNull(message = "Product ID cannot be null")
    private Long productId;
    private boolean used;
}
