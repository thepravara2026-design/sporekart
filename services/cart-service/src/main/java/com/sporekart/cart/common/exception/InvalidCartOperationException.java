package com.sporekart.cart.common.exception;

public class InvalidCartOperationException extends CartException {
    public InvalidCartOperationException(String message) {
        super(message);
    }
}