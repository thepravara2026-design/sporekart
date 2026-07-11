package com.sporekart.ai.core.application.exception;

public class AIGatewayException extends AiCoreException {
    private final String errorCode;

    public AIGatewayException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AIGatewayException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
