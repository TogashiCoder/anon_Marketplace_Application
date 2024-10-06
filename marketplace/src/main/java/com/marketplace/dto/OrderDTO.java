package com.marketplace.dto;

import com.marketplace.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
    private ShipmentDetailsDTO shipmentDetails;
    private List<OrderItemDto> orderItems;
    private Long buyerId;
    private String paypalPaymentId;
}
