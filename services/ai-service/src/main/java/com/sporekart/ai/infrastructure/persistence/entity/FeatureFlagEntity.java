package com.sporekart.ai.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "feature_flags")
public class FeatureFlagEntity {
    @Id
    private UUID id;

    @Column(name = "feature_name", nullable = false, unique = true)
    private String featureName;

    @Column(name = "feature_key", nullable = false, unique = true)
    private String featureKey;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "description")
    private String description;

    @Column(name = "rollout_percentage")
    private Integer rolloutPercentage;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public FeatureFlagEntity() {
    }

    public FeatureFlagEntity(UUID id, String featureName, String featureKey) {
        this.id = id;
        this.featureName = featureName;
        this.featureKey = featureKey;
        this.isEnabled = false;
        this.rolloutPercentage = 0;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
        this.isDeleted = false;
    }
}
