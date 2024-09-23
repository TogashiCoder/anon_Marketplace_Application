package com.marketplace.exception;

public class CouponAlreadyAppliedException extends RuntimeException {
    public CouponAlreadyAppliedException(String message) {
        super(message);
    }
}

