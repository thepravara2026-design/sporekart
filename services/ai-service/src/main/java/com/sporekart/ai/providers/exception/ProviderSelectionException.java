package com.sporekart.ai.providers.exception;

public class ProviderSelectionException extends ProviderException {
    public ProviderSelectionException(String reason) {
        super("PROVIDER_SELECTION_ERROR", "Provider selection failed: " + reason, 500);
    }
}
