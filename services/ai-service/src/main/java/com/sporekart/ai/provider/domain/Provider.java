package com.sporekart.ai.provider.domain;

import java.util.Map;

public record Provider(
        String id,
        String name,
        String providerType,
        boolean enabled,
        Map<String, String> configuration) {
    public Provider(String id, String name, String providerType) {
        this(id, name, providerType, true, Map.of());
    }

    public Provider withConfiguration(Map<String, String> config) {
        return new Provider(id, name, providerType, enabled, config);
    }
}
