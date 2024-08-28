package com.marketplace.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;


@Setter @Getter @ToString
@AllArgsConstructor @NoArgsConstructor
@Entity
public class ShipmentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String carrierName;
    private String trackingNumber;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime shipmentDate;

    @LastModifiedDate
    private LocalDateTime estimatedDeliveryDate;
}
