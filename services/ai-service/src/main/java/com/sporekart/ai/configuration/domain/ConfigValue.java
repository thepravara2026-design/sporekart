package com.sporekart.ai.configuration.domain;

import java.time.Instant;

public record ConfigValue<T>(
    ConfigKey key,
    T value,
    ConfigurationSource source,
    ConfigurationStatus status,
    Instant resolvedAt,
    String resolvedBy,
    String tenantId,
    long version
) {
    public static <T> ConfigValue<T> of(ConfigKey key, T value) {
        return new ConfigValue<>(key, value, ConfigurationSource.DEFAULT,
            ConfigurationStatus.ACTIVE, Instant.now(), "system", null, 1);
    }

    public ConfigValue<T> withSource(ConfigurationSource source) {
        return new ConfigValue<>(key, value, source, status, resolvedAt, resolvedBy, tenantId, version);
    }

    public ConfigValue<T> withTenant(String tenantId) {
        return new ConfigValue<>(key, value, source, status, resolvedAt, resolvedBy, tenantId, version);
    }
}
