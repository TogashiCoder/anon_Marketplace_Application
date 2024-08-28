package com.marketplace.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Coupon {
    @Id
    @GeneratedValue
    private Long id;
    private String code;
    private Float discountPercentage;
    private Date startDate;
    private Date endDate;
    private Integer maxRedemptions;
    private Integer redeemCount;

    @OneToMany(mappedBy = "coupon")
    private List<Product> products;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private List<CouponUsage> couponUsages;
}
