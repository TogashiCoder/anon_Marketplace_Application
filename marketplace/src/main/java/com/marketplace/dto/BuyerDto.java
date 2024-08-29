package com.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.marketplace.enums.Gender;
import com.marketplace.enums.Role;
import com.marketplace.model.*;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuyerDto extends UserDto{
     @JsonIgnore
    private List<Favorite> favorites;
    @JsonIgnore
    private List<Follower> following;
    @JsonIgnore
    private List<Comment> comments;
    @JsonIgnore
    private List<CouponUsage> couponUsages;
    @JsonIgnore
    private ShoppingCart shoppingCart;


}