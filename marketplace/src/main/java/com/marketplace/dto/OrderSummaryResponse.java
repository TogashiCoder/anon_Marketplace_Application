package com.marketplace.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderSummaryResponse {
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private List<OrderDisplayDto> orders;
}
