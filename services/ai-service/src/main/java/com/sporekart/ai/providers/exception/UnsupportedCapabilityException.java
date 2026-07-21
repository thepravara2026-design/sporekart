package com.sporekart.ai.providers.exception;

public class UnsupportedCapabilityException extends ProviderException {
    public UnsupportedCapabilityException(String providerId, String capability) {
        super("UNSUPPORTED_CAPABILITY", "Provider " + providerId + " does not support: " + capability, 400);
    }
}
