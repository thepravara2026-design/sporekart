package com.sporekart.ai.providers.exception;

public class ProviderHealthException extends ProviderException {
    public ProviderHealthException(String providerId, String detail) {
        super("PROVIDER_HEALTH_ERROR", "Health check failed for provider " + providerId + ": " + detail, 503);
    }
}
