package com.sporekart.ai.capabilitydiscovery.interfaces.rest.dto;

import com.sporekart.ai.capabilitydiscovery.domain.CapabilityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CapabilityRequestDto {

    private String capabilityId;

    @NotBlank
    private String capabilityName;

    @NotNull
    private CapabilityType capabilityType;

    private String description;
    private String module;
    private List<String> supportedFeatures = new ArrayList<>();
    private List<String> dependencies = new ArrayList<>();
    private String version;
    private List<String> providerCompatibility = new ArrayList<>();
    private Map<String, String> metadata = new HashMap<>();
    private String featureFlag;
    private boolean enabled;

    public CapabilityRequestDto() {
    }

    public String getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
    }

    public String getCapabilityName() {
        return capabilityName;
    }

    public void setCapabilityName(String capabilityName) {
        this.capabilityName = capabilityName;
    }

    public CapabilityType getCapabilityType() {
        return capabilityType;
    }

    public void setCapabilityType(CapabilityType capabilityType) {
        this.capabilityType = capabilityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public List<String> getSupportedFeatures() {
        return supportedFeatures;
    }

    public void setSupportedFeatures(List<String> supportedFeatures) {
        this.supportedFeatures = supportedFeatures;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getProviderCompatibility() {
        return providerCompatibility;
    }

    public void setProviderCompatibility(List<String> providerCompatibility) {
        this.providerCompatibility = providerCompatibility;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getFeatureFlag() {
        return featureFlag;
    }

    public void setFeatureFlag(String featureFlag) {
        this.featureFlag = featureFlag;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
