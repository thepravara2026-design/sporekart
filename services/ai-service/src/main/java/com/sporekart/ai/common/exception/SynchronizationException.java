package com.sporekart.ai.common.exception;

public class SynchronizationException extends RuntimeException {
    public SynchronizationException(String message) {
        super(message);
    }

    public SynchronizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
