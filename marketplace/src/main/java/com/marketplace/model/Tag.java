package com.marketplace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Tag {
    @Id
    @GeneratedValue
    private Long id;
    private String keyword;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;}
