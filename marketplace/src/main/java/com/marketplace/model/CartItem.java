package com.marketplace.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    //be carful
    @JsonBackReference
    private ShoppingCart shoppingCart;

    private Integer quantity;
    private BigDecimal price;

    //THE update
    @ManyToOne
    @JoinColumn(name = "applied_coupon_id")
    private Coupon appliedCoupon;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountedPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;
}
