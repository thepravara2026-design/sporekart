package com.sporekart.ai.core.application.exception;

public class AIExecutionException extends AIGatewayException {
    public AIExecutionException(String message) {
        super("AI-014", message);
    }

    public AIExecutionException(String errorCode, String message) {
        super(errorCode, message);
    }

    public AIExecutionException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
