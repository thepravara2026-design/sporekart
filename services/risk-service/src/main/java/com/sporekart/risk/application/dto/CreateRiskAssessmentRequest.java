package com.sporekart.risk.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateRiskAssessmentRequest(
        @NotBlank @Size(max = 50) String entityType,
        @NotBlank @Size(max = 100) String entityId,
        @Min(0) @Max(100) int riskScore,
        @NotEmpty List<@Size(min = 1, max = 255) String> factors,
        @NotBlank @Size(max = 100) String assessedBy) {
}