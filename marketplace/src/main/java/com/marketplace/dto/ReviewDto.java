package com.marketplace.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@RequiredArgsConstructor
public class ReviewDto {
    private String ProductName;
    private String BuyerName;
    private String BuyerProfilImageUrl;
    private BigDecimal value;
    private String comment;
    private LocalDateTime createdAt;
}
