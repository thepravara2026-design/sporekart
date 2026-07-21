package com.sporekart.ai.configuration.api;

import com.sporekart.ai.configuration.domain.ConfigKey;
import com.sporekart.ai.configuration.domain.ConfigValue;
import com.sporekart.ai.configuration.domain.ConfigurationSource;

import java.util.Map;
import java.util.Optional;

public interface ConfigurationLoader {
    <T> Optional<ConfigValue<T>> load(ConfigKey key);
    <T> ConfigValue<T> loadOrDefault(ConfigKey key, T defaultValue);
    Map<ConfigKey, ConfigValue<?>> loadAll(String module);
    Map<ConfigKey, ConfigValue<?>> loadAll();
    void reload(String module);
    void reloadAll();
    ConfigurationSource getSource(ConfigKey key);
    boolean isLoaded(ConfigKey key);
}
