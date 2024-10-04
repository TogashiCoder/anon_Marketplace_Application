package com.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marketplace.model.Buyer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FollowerDTO {

    private Long id;
    @NotNull(message = "Buyer ID cannot be null")
    private Long buyerId;
    @NotNull(message = "Seller ID cannot be null")
    private Long sellerId;

//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    private Buyer buyer;
}
