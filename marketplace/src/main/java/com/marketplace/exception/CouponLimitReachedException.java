package com.marketplace.exception;

public class CouponLimitReachedException extends RuntimeException {
    public CouponLimitReachedException(String message) {
        super(message);
    }
}
