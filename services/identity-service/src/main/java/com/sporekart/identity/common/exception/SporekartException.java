package com.sporekart.identity.common.exception;

public abstract class SporekartException extends RuntimeException {

    protected SporekartException(String message) {
        super(message);
    }

    protected SporekartException(String message, Throwable cause) {
        super(message, cause);
    }
}
