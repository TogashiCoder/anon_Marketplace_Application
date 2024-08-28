package com.marketplace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue
    private Long id;
    private String imageUrl;
    private String cloudinaryImageId;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
