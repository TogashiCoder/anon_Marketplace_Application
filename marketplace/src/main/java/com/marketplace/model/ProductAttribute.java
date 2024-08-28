package com.marketplace.model;

import com.marketplace.enums.AttributeType;
import jakarta.persistence.*;
import lombok.*;

@Setter @Getter @ToString
@AllArgsConstructor @NoArgsConstructor
@Entity
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String value;

    @ManyToOne
    @JoinColumn(name = "product_variation_id")
    private ProductVariation productVariation;

    @Enumerated(EnumType.STRING)
    private AttributeType type;

}
