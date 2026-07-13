package com.sporekart.ai.providerregistry.infrastructure.persistence;

import com.sporekart.ai.providerregistry.domain.ProviderCapability;
import com.sporekart.ai.providerregistry.domain.ProviderHealthStatus;
import com.sporekart.ai.providerregistry.domain.ProviderModelInfo;
import com.sporekart.ai.providerregistry.domain.ProviderRegistryEntry;
import com.sporekart.ai.providerregistry.domain.ProviderStatus;
import com.sporekart.ai.providerregistry.domain.ProviderType;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "pr_provider_registry")
public class ProviderRegistryEntity {

    @Id
    @Column(name = "provider_id", nullable = false, length = 64)
    private String providerId;

    @Column(name = "provider_name", nullable = false, length = 255)
    private String providerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", nullable = false, length = 50)
    private ProviderType providerType;

    @Column(name = "version", length = 50)
    private String version;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ProviderStatus status;

    @Column(name = "priority")
    private int priority;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pr_provider_models",
            joinColumns = @JoinColumn(name = "provider_id"))
    @OrderColumn(name = "model_order")
    private List<ProviderModelInfoEmbeddable> supportedModels = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pr_provider_capabilities",
            joinColumns = @JoinColumn(name = "provider_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "capability", length = 50)
    private List<ProviderCapability> capabilities = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "health_status", length = 50)
    private ProviderHealthStatus healthStatus;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, String> metadata = new HashMap<>();

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deprecated_at")
    private Instant deprecatedAt;

    public ProviderRegistryEntity() {}

    public ProviderRegistryEntity(String providerId, String providerName, ProviderType providerType,
                                  String version, ProviderStatus status, int priority,
                                  List<ProviderModelInfoEmbeddable> supportedModels,
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

    public static ProviderRegistryEntity fromDomain(ProviderRegistryEntry entry) {
        List<ProviderModelInfoEmbeddable> models = new ArrayList<>();
        if (entry.getSupportedModels() != null) {
            for (ProviderModelInfo m : entry.getSupportedModels()) {
                models.add(new ProviderModelInfoEmbeddable(
                        m.modelId(), m.modelName(), m.contextWindow(), m.maxTokens(),
                        m.streamingSupported(), m.toolCallingSupported(),
                        m.embeddingsSupported(), m.imageSupported(), m.audioSupported()));
            }
        }
        Map<String, String> meta = entry.getMetadata() != null ? new HashMap<>(entry.getMetadata()) : new HashMap<>();
        return new ProviderRegistryEntity(
                entry.getProviderId(), entry.getProviderName(), entry.getProviderType(),
                entry.getVersion(), entry.getStatus(), entry.getPriority(),
                models,
                entry.getCapabilities() != null ? new ArrayList<>(entry.getCapabilities()) : new ArrayList<>(),
                entry.getHealthStatus(), meta,
                entry.getCreatedAt(), entry.getUpdatedAt(), entry.getDeprecatedAt());
    }

    public ProviderRegistryEntry toDomain() {
        List<ProviderModelInfo> models = new ArrayList<>();
        for (ProviderModelInfoEmbeddable m : supportedModels) {
            models.add(new ProviderModelInfo(
                    m.getModelId(), m.getModelName(), m.getContextWindow(), m.getMaxTokens(),
                    m.isStreamingSupported(), m.isToolCallingSupported(),
                    m.isEmbeddingsSupported(), m.isImageSupported(), m.isAudioSupported()));
        }
        return new ProviderRegistryEntry(
                providerId, providerName, providerType, version, status, priority,
                models,
                new ArrayList<>(capabilities),
                healthStatus, new HashMap<>(metadata),
                createdAt, updatedAt, deprecatedAt);
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

    public List<ProviderModelInfoEmbeddable> getSupportedModels() {
        return supportedModels;
    }

    public void setSupportedModels(List<ProviderModelInfoEmbeddable> supportedModels) {
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
