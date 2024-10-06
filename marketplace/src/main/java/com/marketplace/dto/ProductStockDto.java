package com.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductStockDto {
    String name;
    Integer stockQuantity;
    BigDecimal price;
}
