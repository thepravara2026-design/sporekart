package com.sporekart.report.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record Report(
    String id,
    String title,
    String description,
    ReportType type,
    ReportCategory category,
    ReportStatus status,
    String owner,
    String summary,
    String businessHealth,
    List<String> recommendations,
    List<String> risks,
    Map<String, Object> kpis,
    Map<String, Object> supportingMetrics,
    String templateId,
    String traceId,
    long executionTimeMs,
    Instant generatedAt,
    Instant createdAt
) {
    public static Report create(
        String title, String description, ReportType type, ReportCategory category,
        String owner, String summary, String businessHealth,
        List<String> recommendations, List<String> risks,
        Map<String, Object> kpis, Map<String, Object> supportingMetrics,
        String templateId
    ) {
        return new Report(
            UUID.randomUUID().toString(),
            title, description, type, category, ReportStatus.GENERATED,
            owner, summary, businessHealth,
            recommendations != null ? List.copyOf(recommendations) : List.of(),
            risks != null ? List.copyOf(risks) : List.of(),
            kpis != null ? Map.copyOf(kpis) : Map.of(),
            supportingMetrics != null ? Map.copyOf(supportingMetrics) : Map.of(),
            templateId, UUID.randomUUID().toString(),
            0L, Instant.now(), Instant.now()
        );
    }

    public Report withStatus(ReportStatus newStatus) {
        return new Report(id, title, description, type, category, newStatus,
            owner, summary, businessHealth, recommendations, risks,
            kpis, supportingMetrics, templateId, traceId, executionTimeMs,
            generatedAt, createdAt);
    }

    public Report withExecutionTime(long ms) {
        return new Report(id, title, description, type, category, status,
            owner, summary, businessHealth, recommendations, risks,
            kpis, supportingMetrics, templateId, traceId, ms,
            generatedAt, createdAt);
    }

    public Report withGeneratedAt(Instant now) {
        return new Report(id, title, description, type, category, status,
            owner, summary, businessHealth, recommendations, risks,
            kpis, supportingMetrics, templateId, traceId, executionTimeMs,
            now, createdAt);
    }
}
