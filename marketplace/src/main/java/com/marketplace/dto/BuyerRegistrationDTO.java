package com.marketplace.dto;

import com.marketplace.enums.Gender;
import com.marketplace.enums.Role;
import com.marketplace.model.Address;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuyerRegistrationDTO {
    private String firstname;
    private String lastname;
    private Gender gender;
    private String username;
    private String email;
    private String phone;
    private String password;
    private Role role;
    private String street;
    private String city;
    private String state;
    private String zipcode;
    private String country;
}