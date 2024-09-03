package com.marketplace.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter @Getter @ToString
@AllArgsConstructor @NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private Integer rating;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
