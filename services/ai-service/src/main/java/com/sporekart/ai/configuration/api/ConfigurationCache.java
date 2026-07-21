package com.sporekart.ai.configuration.api;

import com.sporekart.ai.configuration.domain.ConfigKey;
import com.sporekart.ai.configuration.domain.ConfigValue;

import java.time.Duration;
import java.util.Optional;

public interface ConfigurationCache {
    <T> Optional<ConfigValue<T>> get(ConfigKey key);
    <T> void put(ConfigKey key, ConfigValue<T> value);
    <T> void put(ConfigKey key, ConfigValue<T> value, Duration ttl);
    void invalidate(ConfigKey key);
    void invalidateModule(String module);
    void invalidateAll();
    boolean contains(ConfigKey key);
    long size();
    void setRefreshStrategy(RefreshStrategy strategy);
    void setVersioningEnabled(boolean enabled);

    enum RefreshStrategy {
        CACHE_ASIDE,
        WRITE_THROUGH,
        REFRESH_AHEAD,
        NONE
    }
}
