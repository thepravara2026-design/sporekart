package com.sporekart.ai.providers.exception;

public class ProviderConfigurationException extends ProviderException {
    public ProviderConfigurationException(String providerId, String detail) {
        super("PROVIDER_CONFIG_ERROR", "Configuration error for provider " + providerId + ": " + detail, 500);
    }
}
