package com.sporekart.ai.providerregistry.interfaces.rest.dto;

import com.sporekart.ai.providerregistry.domain.ProviderCapability;
import com.sporekart.ai.providerregistry.domain.ProviderType;

import jakarta.validation.constraints.NotNull;

public class ProviderDiscoveryRequestDto {

    @NotNull
    private ProviderCapability capability;

    private ProviderType preferredType;

    public ProviderDiscoveryRequestDto() {}

    public ProviderDiscoveryRequestDto(ProviderCapability capability, ProviderType preferredType) {
        this.capability = capability;
        this.preferredType = preferredType;
    }

    public ProviderCapability getCapability() {
        return capability;
    }

    public void setCapability(ProviderCapability capability) {
        this.capability = capability;
    }

    public ProviderType getPreferredType() {
        return preferredType;
    }

    public void setPreferredType(ProviderType preferredType) {
        this.preferredType = preferredType;
    }
}
