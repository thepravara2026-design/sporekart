package com.sporekart.ai.provider.domain;

import java.util.Map;

public record ProviderConfigurationRecord(
        String providerType,
        String environment,
        boolean enabled,
        int priority,
        int maxRetries,
        long timeoutMs,
        Map<String, String> properties) {
}
