package com.sporekart.ai.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class FeatureFlag {
    private UUID id;
    private String featureName;
    private String featureKey;
    private boolean isEnabled;
    private String description;
    private Integer rolloutPercentage;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;

    public FeatureFlag(UUID id, String featureName, String featureKey) {
        this.id = id;
        this.featureName = featureName;
        this.featureKey = featureKey;
        this.isEnabled = false;
        this.rolloutPercentage = 0;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getFeatureKey() {
        return featureKey;
    }

    public String getFeatureName() {
        return featureName;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public Integer getRolloutPercentage() {
        return rolloutPercentage;
    }

    public void enable() {
        this.isEnabled = true;
        this.updatedAt = OffsetDateTime.now();
    }

    public void disable() {
        this.isEnabled = false;
        this.updatedAt = OffsetDateTime.now();
    }

    public void setRollout(Integer percentage) {
        this.rolloutPercentage = percentage;
        this.updatedAt = OffsetDateTime.now();
    }

    public boolean isActive() {
        if (!isEnabled)
            return false;
        return rolloutPercentage >= 100 || Math.random() * 100 < rolloutPercentage;
    }
}
