package com.sporekart.ai.configuration.model.feature;

import com.sporekart.ai.configuration.domain.ConfigurationSource;

public record FeatureFlag(
    String name,
    String description,
    boolean defaultValue,
    boolean currentValue,
    FeatureScope scope,
    boolean environmentOverride,
    boolean tenantOverride,
    boolean runtimeMutable,
    ConfigurationSource source
) {
    public static FeatureFlag of(String name, String description, boolean defaultValue) {
        return new FeatureFlag(name, description, defaultValue, defaultValue,
            FeatureScope.GLOBAL, true, false, false, ConfigurationSource.DEFAULT);
    }

    public FeatureFlag withScope(FeatureScope scope) {
        return new FeatureFlag(name, description, defaultValue, currentValue,
            scope, environmentOverride, tenantOverride, runtimeMutable, source);
    }

    public FeatureFlag withRuntimeMutable() {
        return new FeatureFlag(name, description, defaultValue, currentValue,
            scope, environmentOverride, tenantOverride, true, source);
    }
}
