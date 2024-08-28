package com.marketplace.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Setter @Getter @ToString
@AllArgsConstructor @NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;
}
