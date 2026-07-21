package com.sporekart.ai.configuration.model.feature;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class FeatureFlagRegistry {
    private final Map<String, FeatureFlag> flags = new LinkedHashMap<>();

    public FeatureFlagRegistry register(FeatureFlag flag) {
        flags.put(flag.name(), flag);
        return this;
    }

    public Optional<FeatureFlag> get(String name) {
        return Optional.ofNullable(flags.get(name));
    }

    public Collection<FeatureFlag> all() {
        return flags.values();
    }

    public boolean isEnabled(String name) {
        return flags.getOrDefault(name, FeatureFlag.of(name, "", false)).currentValue();
    }

    public int size() {
        return flags.size();
    }
}
