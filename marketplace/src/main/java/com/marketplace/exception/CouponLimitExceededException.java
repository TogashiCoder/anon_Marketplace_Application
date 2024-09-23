package com.marketplace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CouponLimitExceededException extends RuntimeException {
    public CouponLimitExceededException(String message) {
        super(message);
    }
}
