package com.sporekart.ai.common.exception;

public class BiometricException extends RuntimeException {
    public BiometricException(String message) {
        super(message);
    }

    public BiometricException(String message, Throwable cause) {
        super(message, cause);
    }
}
