package com.marketplace.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private BigDecimal totalPrice;
}
