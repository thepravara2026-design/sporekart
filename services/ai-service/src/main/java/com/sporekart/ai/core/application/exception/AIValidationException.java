package com.sporekart.ai.core.application.exception;

public class AIValidationException extends AIGatewayException {
    public AIValidationException(String message) {
        super("AI-006", message);
    }

    public AIValidationException(String errorCode, String message) {
        super(errorCode, message);
    }
}
