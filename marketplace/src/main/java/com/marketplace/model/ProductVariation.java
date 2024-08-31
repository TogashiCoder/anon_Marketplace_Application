package com.marketplace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class ProductVariation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String sku;
    private BigDecimal price;
    private Integer stockQuantity;

    @OneToMany(mappedBy = "variation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VariationAttributeValue> attributeValues = new ArrayList<>();

}

