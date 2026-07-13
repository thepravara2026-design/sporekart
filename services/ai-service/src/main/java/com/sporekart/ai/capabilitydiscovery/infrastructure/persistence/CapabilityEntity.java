package com.sporekart.ai.capabilitydiscovery.infrastructure.persistence;

import com.sporekart.ai.capabilitydiscovery.domain.CapabilityAvailability;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "cd_capability")
public class CapabilityEntity {

    @Id
    @Column(name = "capability_id", nullable = false, length = 255)
    private String capabilityId;

    @Column(name = "capability_name", nullable = false, length = 255)
    private String capabilityName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "capability_type", nullable = false, length = 64)
    private CapabilityType capabilityType;

    @Column(name = "module", length = 255)
    private String module;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cd_capability_features",
            joinColumns = @JoinColumn(name = "capability_id"))
    @Column(name = "feature", length = 255)
    private List<String> supportedFeatures = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cd_capability_dependencies",
            joinColumns = @JoinColumn(name = "capability_id"))
    @Column(name = "dependency", length = 255)
    private List<String> dependencies = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "availability", length = 32)
    private CapabilityAvailability availability;

    @Column(name = "version", length = 64)
    private String version;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cd_capability_providers",
            joinColumns = @JoinColumn(name = "capability_id"))
    @Column(name = "provider", length = 255)
    private List<String> providerCompatibility = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cd_capability_metadata",
            joinColumns = @JoinColumn(name = "capability_id"))
    @MapKeyColumn(name = "metadata_key", length = 255)
    @Column(name = "metadata_value", length = 2048)
    private Map<String, String> metadata = new HashMap<>();

    @Column(name = "feature_flag", length = 255)
    private String featureFlag;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public CapabilityEntity() {
    }

    public CapabilityEntity(String capabilityId, String capabilityName, CapabilityType capabilityType) {
        this.capabilityId = capabilityId;
        this.capabilityName = capabilityName;
        this.capabilityType = capabilityType;
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
