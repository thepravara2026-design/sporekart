package com.sporekart.ai.core.application.exception;

public abstract class AiCoreException extends RuntimeException {
    protected AiCoreException(String message) {
        super(message);
    }

    protected AiCoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
