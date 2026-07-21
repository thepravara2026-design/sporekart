package com.sporekart.ai.providers.exception;

public class ProviderDiscoveryException extends ProviderException {
    public ProviderDiscoveryException(String detail) {
        super("PROVIDER_DISCOVERY_ERROR", "Provider discovery failed: " + detail, 500);
    }
}
