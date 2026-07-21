package com.sporekart.ai.configuration.binder;

import com.sporekart.ai.configuration.domain.ConfigKey;

public interface ConfigPropertyBinder {
    <T> T bind(String prefix, Class<T> targetType);
    <T> T bind(String prefix, String profile, Class<T> targetType);
    <T> T bindWithOverrides(String prefix, Class<T> targetType, String... overridePrefixes);
    void validateBinding(String prefix, Class<?> targetType);
    boolean isBound(String prefix);
}
