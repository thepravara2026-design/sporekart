package com.sporekart.ai.provider.models;

import java.util.Map;
import java.util.Optional;

public record ProviderRequest(
        String prompt,
        String model,
        String providerType,
        Map<String, String> parameters,
        Map<String, String> headers,
        Optional<String> userIdentifier,
        boolean stream) {

    public ProviderRequest {
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("Prompt must not be blank");
        }
        parameters = parameters == null ? Map.of() : Map.copyOf(parameters);
        headers = headers == null ? Map.of() : Map.copyOf(headers);
        userIdentifier = userIdentifier == null ? Optional.empty() : userIdentifier;
    }

    public static ProviderRequest of(String prompt, String model) {
        return new ProviderRequest(prompt, model, null, Map.of(), Map.of(), Optional.empty(), false);
    }

    public static ProviderRequest of(String prompt, String model, String providerType) {
        return new ProviderRequest(prompt, model, providerType, Map.of(), Map.of(), Optional.empty(), false);
    }

    public ProviderRequest withParameter(String key, String value) {
        var mutable = new java.util.HashMap<>(parameters());
        mutable.put(key, value);
        return new ProviderRequest(prompt, model, providerType, mutable, headers, userIdentifier, stream);
    }

    public ProviderRequest withStream(boolean stream) {
        return new ProviderRequest(prompt, model, providerType, parameters, headers, userIdentifier, stream);
    }
}
