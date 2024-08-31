package com.marketplace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FollowerNotFoundException extends RuntimeException {
    public FollowerNotFoundException(String buyerId, String sellerId) {
        super(String.format("Follower relationship not found with Buyer ID: '%s' and Seller ID: '%s'", buyerId, sellerId));
    }
}
