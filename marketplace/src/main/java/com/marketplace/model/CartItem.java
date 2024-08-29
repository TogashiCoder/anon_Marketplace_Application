package com.marketplace.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Setter @Getter @ToString
@AllArgsConstructor @NoArgsConstructor
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;

    private Integer quantity;
    private BigDecimal price;
}