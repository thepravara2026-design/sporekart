package com.sporekart.risk.infrastructure.persistence;

import com.sporekart.risk.domain.model.RiskAssessment;
import com.sporekart.risk.domain.model.RiskLevel;
import com.sporekart.risk.domain.model.RiskStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "risk_assessments")
public class RiskAssessmentEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "entity_type", nullable = false, length = 100)
    private String entityType;

    @Column(name = "entity_id", nullable = false, length = 100)
    private String entityId;

    @Column(name = "risk_score", nullable = false)
    private int riskScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 30)
    private RiskLevel riskLevel;

    @Column(name = "factors", columnDefinition = "TEXT")
    private String factors;

    @Column(name = "assessed_by", length = 100)
    private String assessedBy;

    @Column(name = "assessed_at")
    private Instant assessedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private RiskStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected RiskAssessmentEntity() {}

    public RiskAssessmentEntity(String id, String entityType, String entityId, int riskScore, RiskLevel riskLevel,
                                String factors, String assessedBy, Instant assessedAt, RiskStatus status,
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

    public static RiskAssessmentEntity fromDomain(RiskAssessment assessment) {
        return new RiskAssessmentEntity(
            assessment.getId(), assessment.getEntityType(), assessment.getEntityId(),
            assessment.getRiskScore(), assessment.getRiskLevel(),
            String.join(",", assessment.getFactors()),
            assessment.getAssessedBy(), assessment.getAssessedAt(),
            assessment.getStatus(), assessment.getCreatedAt(), assessment.getUpdatedAt());
    }

    public RiskAssessment toDomain() {
        List<String> factorList = factors != null && !factors.isEmpty()
            ? List.of(factors.split(",", -1))
            : List.of();
        return new RiskAssessment(id, entityType, entityId, riskScore, riskLevel,
            factorList, assessedBy, assessedAt, status, createdAt, updatedAt);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public int getRiskScore() { return riskScore; }
    public void setRiskScore(int riskScore) { this.riskScore = riskScore; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
    public String getFactors() { return factors; }
    public void setFactors(String factors) { this.factors = factors; }
    public String getAssessedBy() { return assessedBy; }
    public void setAssessedBy(String assessedBy) { this.assessedBy = assessedBy; }
    public Instant getAssessedAt() { return assessedAt; }
    public void setAssessedAt(Instant assessedAt) { this.assessedAt = assessedAt; }
    public RiskStatus getStatus() { return status; }
    public void setStatus(RiskStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
