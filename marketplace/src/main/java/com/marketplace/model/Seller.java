package com.marketplace.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@DiscriminatorValue("SELLER")
public class Seller extends User {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shopCoverImage_Id")
    private Image shopCoverImage;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follower> followers;
}
