package com.marketplace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateProductException extends RuntimeException {
    public DuplicateProductException(String productId) {
        super(String.format("Product with ID '%s' is already in the shopping cart.", productId));
    }
}
