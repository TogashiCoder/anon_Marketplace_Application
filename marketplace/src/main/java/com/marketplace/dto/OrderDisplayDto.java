package com.marketplace.dto;

import com.marketplace.enums.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDisplayDto {
    private String id;
    private String customer;
    private String date;
    private BigDecimal total;
    private String status;
}
