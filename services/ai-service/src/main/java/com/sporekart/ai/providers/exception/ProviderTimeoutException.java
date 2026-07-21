package com.sporekart.ai.providers.exception;

public class ProviderTimeoutException extends ProviderException {
    public ProviderTimeoutException(String providerId, long timeoutMs) {
        super("PROVIDER_TIMEOUT", "Provider " + providerId + " timed out after " + timeoutMs + "ms", 504);
    }
}
