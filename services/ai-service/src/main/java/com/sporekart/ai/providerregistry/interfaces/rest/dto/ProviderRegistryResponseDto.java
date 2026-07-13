package com.sporekart.ai.providerregistry.interfaces.rest.dto;

import com.sporekart.ai.providerregistry.domain.ProviderCapability;
import com.sporekart.ai.providerregistry.domain.ProviderHealthStatus;
import com.sporekart.ai.providerregistry.domain.ProviderModelInfo;
import com.sporekart.ai.providerregistry.domain.ProviderRegistryEntry;
import com.sporekart.ai.providerregistry.domain.ProviderStatus;
import com.sporekart.ai.providerregistry.domain.ProviderType;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ProviderRegistryResponseDto {

    private String providerId;
    private String providerName;
    private ProviderType providerType;
    private String version;
    private ProviderStatus status;
    private int priority;
    private List<ProviderModelInfo> supportedModels;
    private List<ProviderCapability> capabilities;
    private ProviderHealthStatus healthStatus;
    private Map<String, String> metadata;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deprecatedAt;

    public ProviderRegistryResponseDto() {}

    public ProviderRegistryResponseDto(String providerId, String providerName, ProviderType providerType,
                                       String version, ProviderStatus status, int priority,
                                       List<ProviderModelInfo> supportedModels,
                                       List<ProviderCapability> capabilities,
                                       ProviderHealthStatus healthStatus, Map<String, String> metadata,
                                       Instant createdAt, Instant updatedAt, Instant deprecatedAt) {
        this.providerId = providerId;
        this.providerName = providerName;
        this.providerType = providerType;
        this.version = version;
        this.status = status;
        this.priority = priority;
        this.supportedModels = supportedModels;
        this.capabilities = capabilities;
        this.healthStatus = healthStatus;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deprecatedAt = deprecatedAt;
    }

    public static ProviderRegistryResponseDto from(ProviderRegistryEntry entry) {
        return new ProviderRegistryResponseDto(
                entry.getProviderId(), entry.getProviderName(), entry.getProviderType(),
                entry.getVersion(), entry.getStatus(), entry.getPriority(),
                entry.getSupportedModels(), entry.getCapabilities(), entry.getHealthStatus(),
                entry.getMetadata(), entry.getCreatedAt(), entry.getUpdatedAt(), entry.getDeprecatedAt());
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ProviderStatus getStatus() {
        return status;
    }

    public void setStatus(ProviderStatus status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<ProviderModelInfo> getSupportedModels() {
        return supportedModels;
    }

    public void setSupportedModels(List<ProviderModelInfo> supportedModels) {
        this.supportedModels = supportedModels;
    }

    public List<ProviderCapability> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<ProviderCapability> capabilities) {
        this.capabilities = capabilities;
    }

    public ProviderHealthStatus getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(ProviderHealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
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

    public Instant getDeprecatedAt() {
        return deprecatedAt;
    }

    public void setDeprecatedAt(Instant deprecatedAt) {
        this.deprecatedAt = deprecatedAt;
    }
}
