package com.sporekart.cart.common.exception;

public abstract class CartException extends RuntimeException {
    protected CartException(String message) {
        super(message);
    }
}