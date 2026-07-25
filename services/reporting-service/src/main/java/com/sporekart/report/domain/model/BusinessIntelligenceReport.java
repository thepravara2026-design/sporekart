package com.sporekart.report.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record BusinessIntelligenceReport(
    String id,
    String title,
    String description,
    ReportType type,
    ReportCategory category,
    String executiveSummary,
    String businessHealth,
    List<String> recommendations,
    List<String> riskFlags,
    Map<String, Object> aggregatedKpis,
    Map<String, Object> supportingMetrics,
    List<Map<String, Object>> dataSources,
    Instant generatedAt,
    Instant createdAt
) {
    public static BusinessIntelligenceReport create(
        String title, String description, ReportType type, ReportCategory category,
        String executiveSummary, String businessHealth,
        List<String> recommendations, List<String> riskFlags,
        Map<String, Object> aggregatedKpis, Map<String, Object> supportingMetrics,
        List<Map<String, Object>> dataSources
    ) {
        return new BusinessIntelligenceReport(
            UUID.randomUUID().toString(),
            title, description, type, category,
            executiveSummary, businessHealth,
            recommendations != null ? List.copyOf(recommendations) : List.of(),
            riskFlags != null ? List.copyOf(riskFlags) : List.of(),
            aggregatedKpis != null ? Map.copyOf(aggregatedKpis) : Map.of(),
            supportingMetrics != null ? Map.copyOf(supportingMetrics) : Map.of(),
            dataSources != null ? List.copyOf(dataSources) : List.of(),
            Instant.now(), Instant.now()
        );
    }
}
