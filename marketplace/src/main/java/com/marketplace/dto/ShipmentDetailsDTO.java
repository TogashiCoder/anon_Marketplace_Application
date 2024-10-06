package com.marketplace.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShipmentDetailsDTO {
    private Long id;
    private String carrierName;
    private String trackingNumber;
    private LocalDateTime shipmentDate;
    private LocalDateTime estimatedDeliveryDate;
}
