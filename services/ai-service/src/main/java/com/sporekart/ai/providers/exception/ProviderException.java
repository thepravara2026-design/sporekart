package com.sporekart.ai.providers.exception;

public class ProviderException extends RuntimeException {
    private final String errorCode;
    private final int statusCode;

    public ProviderException(String errorCode, String message, int statusCode) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public ProviderException(String errorCode, String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public String getErrorCode() { return errorCode; }
    public int getStatusCode() { return statusCode; }
}
