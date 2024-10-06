package com.marketplace.dto;

import com.marketplace.enums.OrderStatus;
import com.marketplace.enums.PaymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderConfirmationDto {
    private Long id;
    private String orderNumber;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;
    private LocalDateTime estimatedDeliveryDate;
    private List<OrderItemDto> orderItems;
    private String buyerName;
    private String carrierName;
    private String trackingNumber;
    private PaymentType paymentType;
    private String paymentDetails;
    private String paypalPaymentId;
    private String shippingStreet;
    private String shippingCity;
    private String shippingState;
    private String shippingZipCode;
    private String shippingCountry;
}