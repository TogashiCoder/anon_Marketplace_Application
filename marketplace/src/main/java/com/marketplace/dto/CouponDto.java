package com.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.marketplace.model.CouponUsage;
import com.marketplace.model.Product;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class CouponDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Coupon code cannot be blank")
    @Size(min = 3, max = 50, message = "Coupon code must be between 3 and 50 characters")
    private String code;

    @NotNull(message = "Discount percentage is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount percentage must be greater than 0")
    @DecimalMax(value = "100.0", inclusive = false, message = "Discount percentage must be less than 100")
    private Float discountPercentage;

    @NotNull(message = "Start date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "Max redemptions is required")
    @Min(value = 1, message = "Max redemptions must be at least 1")
    private Integer maxRedemptions;

   @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer redeemCount;

   @JsonIgnore
   private List<Long> productIds = new ArrayList<>();

    @JsonIgnore
    private List<CouponUsage> couponUsages = new ArrayList<>();
}
