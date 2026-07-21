package com.sporekart.ai.gateway.exception;

public class AuthenticationException extends GatewayException {
    public AuthenticationException(String message) {
        super("AUTHENTICATION_FAILED", message, 401);
    }

    public AuthenticationException(String message, Throwable cause) {
        super("AUTHENTICATION_FAILED", message, 401, cause);
    }
}
