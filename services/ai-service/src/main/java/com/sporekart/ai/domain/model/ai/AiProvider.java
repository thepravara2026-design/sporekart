package com.sporekart.ai.domain.model.ai;

import com.sporekart.ai.domain.model.ProviderType;

public class AiProvider {
    private final String id;
    private final ProviderType providerType;
    private final String name;
    private final boolean enabled;
    private final String configuration;

    public AiProvider(String id, ProviderType providerType, String name, boolean enabled, String configuration) {
        this.id = id;
        this.providerType = providerType;
        this.name = name;
        this.enabled = enabled;
        this.configuration = configuration;
    }

    public String getId() {
        return id;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getConfiguration() {
        return configuration;
    }
}

