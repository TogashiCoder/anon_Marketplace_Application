package com.marketplace.model;

import jakarta.persistence.Embeddable;

public class Address {
    private String street;
    private String city;
    private String state;
    private String zipcode;
    private String country;
}
