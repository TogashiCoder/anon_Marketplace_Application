package com.marketplace.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Setter @Getter @ToString
@AllArgsConstructor @NoArgsConstructor
@Entity
public class ProductView {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime viewedAt;
}
