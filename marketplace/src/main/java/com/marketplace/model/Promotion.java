package com.marketplace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Promotion {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private Double discountPercentage;
    private Date startDate;
    private Date endDate;
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
}
