package com.marketplace.exception;

public class CouponCreationException extends RuntimeException {
    public CouponCreationException(String message) {
        super(message);
    }

    public CouponCreationException(String message, Throwable cause) {
        super(message, cause);
    }

}
