package com.marketplace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AlreadyFollowingException extends RuntimeException {
    public AlreadyFollowingException(String buyerId, String sellerId) {
        super(String.format("Buyer with ID: '%s' is already following Seller with ID: '%s'", buyerId, sellerId));
    }
}