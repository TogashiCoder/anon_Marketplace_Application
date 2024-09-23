package com.marketplace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CouponAlreadyUsedException extends RuntimeException {
    public CouponAlreadyUsedException(Long couponId, Long productId, Long buyerId) {
        super(String.format("Coupon with ID %d has already been used by buyer with ID %d for product with ID %d", couponId, buyerId, productId));
    }

    public CouponAlreadyUsedException(String message ) {
        super(message);
    }

}
