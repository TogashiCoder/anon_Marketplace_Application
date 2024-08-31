package com.marketplace.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VariationAttributeValue {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "variation_id")
    private ProductVariation variation;

    @ManyToOne
    @JoinColumn(name = "attribute_id")
    private ProductAttribute attribute;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private AttributeOption option;
}
