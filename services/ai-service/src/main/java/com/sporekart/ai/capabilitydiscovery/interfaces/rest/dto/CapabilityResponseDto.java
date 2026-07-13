package com.sporekart.ai.capabilitydiscovery.interfaces.rest.dto;

import com.sporekart.ai.capabilitydiscovery.domain.CapabilityAvailability;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityEntry;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CapabilityResponseDto {

    private String capabilityId;
    private String capabilityName;
    private String description;
    private CapabilityType capabilityType;
    private String module;
    private List<String> supportedFeatures = new ArrayList<>();
    private List<String> dependencies = new ArrayList<>();
    private CapabilityAvailability availability;
    private String version;
    private List<String> providerCompatibility = new ArrayList<>();
    private Map<String, String> metadata = new HashMap<>();
    private String featureFlag;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;

    public CapabilityResponseDto() {
    }

    public static CapabilityResponseDto from(CapabilityEntry entry) {
        CapabilityResponseDto dto = new CapabilityResponseDto();
        dto.setCapabilityId(entry.getCapabilityId());
        dto.setCapabilityName(entry.getCapabilityName());
        dto.setDescription(entry.getDescription());
        dto.setCapabilityType(entry.getCapabilityType());
        dto.setModule(entry.getModule());
        dto.setSupportedFeatures(entry.getSupportedFeatures() == null
                ? new ArrayList<>() : new ArrayList<>(entry.getSupportedFeatures()));
        dto.setDependencies(entry.getDependencies() == null
                ? new ArrayList<>() : new ArrayList<>(entry.getDependencies()));
        dto.setAvailability(entry.getAvailability());
        dto.setVersion(entry.getVersion());
        dto.setProviderCompatibility(entry.getProviderCompatibility() == null
                ? new ArrayList<>() : new ArrayList<>(entry.getProviderCompatibility()));
        dto.setMetadata(entry.getMetadata() == null
                ? new HashMap<>() : new HashMap<>(entry.getMetadata()));
        dto.setFeatureFlag(entry.getFeatureFlag());
        dto.setEnabled(entry.isEnabled());
        dto.setCreatedAt(entry.getCreatedAt());
        dto.setUpdatedAt(entry.getUpdatedAt());
        return dto;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CapabilityType getCapabilityType() {
        return capabilityType;
    }

    public void setCapabilityType(CapabilityType capabilityType) {
        this.capabilityType = capabilityType;
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

    public CapabilityAvailability getAvailability() {
        return availability;
    }

    public void setAvailability(CapabilityAvailability availability) {
        this.availability = availability;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
