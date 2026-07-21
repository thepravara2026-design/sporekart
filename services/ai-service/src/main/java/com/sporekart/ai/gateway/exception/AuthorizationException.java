package com.sporekart.ai.gateway.exception;

public class AuthorizationException extends GatewayException {
    public AuthorizationException(String message) {
        super("FORBIDDEN", message, 403);
    }
}
