package com.sporekart.ai.gateway.exception;

public class ProviderException extends GatewayException {
    private final String providerId;

    public ProviderException(String providerId, String message) {
        super("PROVIDER_ERROR", message + " [provider: " + providerId + "]", 502);
        this.providerId = providerId;
    }

    public String getProviderId() { return providerId; }
}
