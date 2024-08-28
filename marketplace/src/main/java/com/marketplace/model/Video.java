package com.marketplace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Video {
    @Id
    @GeneratedValue
    private Long id;
    private String videoUrl;
    private String cloudinaryVideoId;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
