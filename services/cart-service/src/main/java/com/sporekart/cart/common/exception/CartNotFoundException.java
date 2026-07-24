package com.sporekart.cart.common.exception;

public class CartNotFoundException extends CartException {
    public CartNotFoundException(String message) {
        super(message);
    }
}