package com.sporekart.ai.gateway.exception;

public class GatewayException extends RuntimeException {
    private final String errorCode;
    private final int statusCode;

    public GatewayException(String errorCode, String message, int statusCode) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public GatewayException(String errorCode, String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public String getErrorCode() { return errorCode; }
    public int getStatusCode() { return statusCode; }
}
