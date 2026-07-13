package com.sporekart.ai.capabilitydiscovery.interfaces.rest.dto;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CapabilityDiscoveryRequestDto {

    @NotNull
    private List<String> requiredFeatures = new ArrayList<>();

    public CapabilityDiscoveryRequestDto() {
    }

    public List<String> getRequiredFeatures() {
        return requiredFeatures;
    }

    public void setRequiredFeatures(List<String> requiredFeatures) {
        this.requiredFeatures = requiredFeatures;
    }
}
