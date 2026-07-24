package com.sporekart.risk.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class RiskAssessment {
    private final String id;
    private final String entityType;
    private final String entityId;
    private final int riskScore;
    private final RiskLevel riskLevel;
    private final List<String> factors;
    private final String assessedBy;
    private final Instant assessedAt;
    private final RiskStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;

    public RiskAssessment(String id, String entityType, String entityId, int riskScore, RiskLevel riskLevel,
            List<String> factors, String assessedBy, Instant assessedAt, RiskStatus status,
            Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.entityType = entityType;
        this.entityId = entityId;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.factors = factors;
        this.assessedBy = assessedBy;
        this.assessedAt = assessedAt;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RiskLevel determineRiskLevel(int riskScore) {
        if (riskScore < 0 || riskScore > 100) {
            throw new IllegalArgumentException("Risk score must be between 0 and 100");
        }
        if (riskScore <= 25) return RiskLevel.LOW;
        if (riskScore <= 50) return RiskLevel.MEDIUM;
        if (riskScore <= 75) return RiskLevel.HIGH;
        return RiskLevel.CRITICAL;
    }

    public static RiskAssessment create(String entityType, String entityId, int riskScore,
            List<String> factors, String assessedBy) {
        if (factors == null || factors.isEmpty()) {
            throw new IllegalArgumentException("At least one risk factor is required");
        }
        if (riskScore < 0 || riskScore > 100) {
            throw new IllegalArgumentException("Risk score must be between 0 and 100");
        }
        String id = UUID.randomUUID().toString();
        Instant now = Instant.now();
        return new RiskAssessment(id, entityType, entityId, riskScore, determineRiskLevel(riskScore),
                List.copyOf(factors), assessedBy, now, RiskStatus.PENDING, now, now);
    }

    public RiskAssessment withStatus(RiskStatus newStatus) {
        return new RiskAssessment(id, entityType, entityId, riskScore, riskLevel, factors,
                assessedBy, assessedAt, newStatus, createdAt, Instant.now());
    }

    public String getId() {
        return id;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public List<String> getFactors() {
        return factors;
    }

    public String getAssessedBy() {
        return assessedBy;
    }

    public Instant getAssessedAt() {
        return assessedAt;
    }

    public RiskStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}