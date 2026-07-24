package com.sporekart.risk.application.dto;

import com.sporekart.risk.domain.model.RiskAssessment;
import com.sporekart.risk.domain.model.RiskLevel;
import com.sporekart.risk.domain.model.RiskStatus;

import java.time.Instant;
import java.util.List;

public record RiskAssessmentResponse(
        String id,
        String entityType,
        String entityId,
        int riskScore,
        RiskLevel riskLevel,
        List<String> factors,
        String assessedBy,
        Instant assessedAt,
        RiskStatus status,
        Instant createdAt,
        Instant updatedAt) {

    public static RiskAssessmentResponse from(RiskAssessment assessment) {
        return new RiskAssessmentResponse(
                assessment.getId(),
                assessment.getEntityType(),
                assessment.getEntityId(),
                assessment.getRiskScore(),
                assessment.getRiskLevel(),
                assessment.getFactors(),
                assessment.getAssessedBy(),
                assessment.getAssessedAt(),
                assessment.getStatus(),
                assessment.getCreatedAt(),
                assessment.getUpdatedAt());
    }
}