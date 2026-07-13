package com.sporekart.ai.risk.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_trust_scores")
public class TrustScoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "assessment_id")
    private UUID assessmentId;

    @Column(name = "overall_trust_score", nullable = false)
    private double overallTrustScore;

    @Column(name = "factor_scores", columnDefinition = "TEXT")
    private String factorScores;

    @Column(name = "factor_reasons", columnDefinition = "TEXT")
    private String factorReasons;

    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public TrustScoreEntity() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getAssessmentId() { return assessmentId; }
    public void setAssessmentId(UUID assessmentId) { this.assessmentId = assessmentId; }
    public double getOverallTrustScore() { return overallTrustScore; }
    public void setOverallTrustScore(double overallTrustScore) { this.overallTrustScore = overallTrustScore; }
    public String getFactorScores() { return factorScores; }
    public void setFactorScores(String factorScores) { this.factorScores = factorScores; }
    public String getFactorReasons() { return factorReasons; }
    public void setFactorReasons(String factorReasons) { this.factorReasons = factorReasons; }
    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
}
