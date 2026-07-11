package com.sporekart.ai.core.application.exception;

public class ProviderNotAvailableException extends AiCoreException {
    private final String providerType;

    public ProviderNotAvailableException(String providerType) {
        super("AI provider [" + providerType + "] is not available");
        this.providerType = providerType;
    }

    public String getProviderType() {
        return providerType;
    }
}
