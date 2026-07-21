package com.sporekart.ai.configuration.api;

import com.sporekart.ai.configuration.domain.ConfigKey;
import com.sporekart.ai.configuration.domain.ConfigValue;

import java.util.Optional;

public interface PropertyResolver {
    <T> Optional<T> resolve(String key, Class<T> targetType);
    <T> T resolveOrDefault(String key, Class<T> targetType, T defaultValue);
    <T> ConfigValue<T> resolveWithMetadata(String key, Class<T> targetType);
    boolean containsKey(String key);
    int size();
}
