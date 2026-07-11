package com.sporekart.ai.common.exception;

public class GSTException extends RuntimeException {
    public GSTException(String message) {
        super(message);
    }

    public GSTException(String message, Throwable cause) {
        super(message, cause);
    }
}
