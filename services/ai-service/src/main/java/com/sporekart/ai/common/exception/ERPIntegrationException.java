package com.sporekart.ai.common.exception;

public class ERPIntegrationException extends RuntimeException {
    public ERPIntegrationException(String message) {
        super(message);
    }

    public ERPIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
