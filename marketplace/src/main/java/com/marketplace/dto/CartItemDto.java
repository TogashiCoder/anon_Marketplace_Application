package com.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CartItemDto {

    private Long id;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Long productId;
    private Integer stockQuantity;
    private Integer minimumOrderQuantity;
    private String productImageUrl;



}
