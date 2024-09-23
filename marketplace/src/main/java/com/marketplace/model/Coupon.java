package com.marketplace.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String code;
    private Float discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxRedemptions;
    private Integer redeemCount;
    @OneToMany(mappedBy = "coupon")
    private List<Product> products = new ArrayList<>();
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private List<CouponUsage> couponUsages = new ArrayList<>();
}
