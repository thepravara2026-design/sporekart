package com.sporekart.ai.providers.exception;

public class ProviderUnavailableException extends ProviderException {
    public ProviderUnavailableException(String providerId) {
        super("PROVIDER_UNAVAILABLE", "Provider " + providerId + " is currently unavailable", 503);
    }
}
