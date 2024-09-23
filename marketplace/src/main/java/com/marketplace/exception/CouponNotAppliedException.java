package com.marketplace.exception;

public class CouponNotAppliedException extends RuntimeException {

    public CouponNotAppliedException(String message) {
        super(message);
    }

    public CouponNotAppliedException(String message, Throwable cause) {
        super(message, cause);
    }
}