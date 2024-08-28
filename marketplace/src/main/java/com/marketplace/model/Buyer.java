package com.marketplace.model;

import jakarta.persistence.*;

import java.util.List;

@DiscriminatorValue("BUYER")
@Entity
public class Buyer extends User{

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follower> following;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;


    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<CouponUsage> couponUsages;


    @OneToOne(mappedBy = "buyer")
    private ShoppingCart shoppingCart;
}
