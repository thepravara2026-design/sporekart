package com.sporekart.ai.configregistry.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "cr_configuration")
public class ConfigurationEntity {

    @Id
    @Column(name = "config_id", nullable = false, length = 64)
    private String configId;

    @Column(name = "config_key", nullable = false, length = 255, unique = true)
    private String configKey;

    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "config_type", length = 50)
    private com.sporekart.ai.configregistry.domain.ConfigType configType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "module", length = 255)
    private String module;

    @Column(name = "environment", length = 128)
    private String environment;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cr_configuration_metadata", joinColumns = @JoinColumn(name = "config_id"))
    @MapKeyColumn(name = "metadata_key")
    @Column(name = "metadata_value", columnDefinition = "TEXT")
    private Map<String, String> metadata = new HashMap<>();

    @Column(name = "snapshot_id", length = 64)
    private String snapshotId;

    @Column(name = "is_valid", nullable = false)
    private boolean valid;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    public ConfigurationEntity() {}

    public ConfigurationEntity(String configId, String configKey, String configValue,
            com.sporekart.ai.configregistry.domain.ConfigType configType, String description,
            int version, String module, String environment, Map<String, String> metadata,
            String snapshotId, boolean valid, Instant createdAt, Instant updatedAt, String createdBy) {
        this.configId = configId;
        this.configKey = configKey;
        this.configValue = configValue;
        this.configType = configType;
        this.description = description;
        this.version = version;
        this.module = module;
        this.environment = environment;
        this.metadata = metadata != null ? metadata : new HashMap<>();
        this.snapshotId = snapshotId;
        this.valid = valid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public com.sporekart.ai.configregistry.domain.ConfigType getConfigType() {
        return configType;
    }

    public void setConfigType(com.sporekart.ai.configregistry.domain.ConfigType configType) {
        this.configType = configType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata != null ? metadata : new HashMap<>();
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
