package com.marketplace.model;

import com.marketplace.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

@Setter @Getter @ToString
@NoArgsConstructor @AllArgsConstructor
@Entity
public class PaymentMethod {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private String details;
    @OneToOne(mappedBy = "paymentMethod")
    private Order order;
}
