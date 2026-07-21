package com.sporekart.ai.configuration.domain;

public record ConfigKey(
    String key,
    String module,
    String description,
    Class<?> type,
    boolean required,
    boolean secret,
    boolean runtimeRefreshable,
    ConfigurationSource source,
    ConfigurationStatus status,
    Object defaultValue
) {
    public ConfigKey {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Config key must not be blank");
        }
    }

    public static ConfigKey of(String key, Class<?> type) {
        return new ConfigKey(key, "global", "", type, false, false, false,
            ConfigurationSource.DEFAULT, ConfigurationStatus.ACTIVE, null);
    }

    public ConfigKey withModule(String module) {
        return new ConfigKey(key, module, description, type, required, secret, runtimeRefreshable,
            source, status, defaultValue);
    }

    public ConfigKey withDescription(String description) {
        return new ConfigKey(key, module, description, type, required, secret, runtimeRefreshable,
            source, status, defaultValue);
    }

    public ConfigKey withDefault(Object defaultValue) {
        return new ConfigKey(key, module, description, type, required, secret, runtimeRefreshable,
            source, status, defaultValue);
    }
}
