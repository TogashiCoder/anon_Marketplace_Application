package com.marketplace.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal value; // Rating value, e.g., 1-5

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @Column(length = 1000)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    private LocalDateTime createdAt;





    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
