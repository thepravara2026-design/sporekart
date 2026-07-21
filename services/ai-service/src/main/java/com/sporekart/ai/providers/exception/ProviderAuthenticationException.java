package com.sporekart.ai.providers.exception;

public class ProviderAuthenticationException extends ProviderException {
    public ProviderAuthenticationException(String providerId) {
        super("PROVIDER_AUTH_ERROR", "Authentication failed for provider " + providerId, 401);
    }
}
