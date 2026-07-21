package com.sporekart.ai.configuration.api;

import com.sporekart.ai.configuration.domain.ConfigKey;
import com.sporekart.ai.configuration.domain.ConfigValue;
import com.sporekart.ai.configuration.domain.ConfigurationSource;

import java.util.Map;
import java.util.Optional;

public interface ConfigurationProvider {
    String name();
    int priority();
    boolean supports(ConfigKey key);
    <T> Optional<ConfigValue<T>> resolve(ConfigKey key);
    Map<ConfigKey, ConfigValue<?>> resolveAll();
    ConfigurationSource source();
    boolean isAvailable();
}
