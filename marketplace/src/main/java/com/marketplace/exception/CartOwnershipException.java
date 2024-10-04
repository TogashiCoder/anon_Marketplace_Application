package com.marketplace.exception;

public class CartOwnershipException extends RuntimeException {
    public CartOwnershipException(String message) {
        super(message);
    }
}