package com.sporekart.alert.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class Alert {
    private final String id;
    private final String title;
    private final String description;
    private final AlertCategory category;
    private final AlertSeverity severity;
    private final AlertPriority priority;
    private final String domain;
    private final AlertStatus status;
    private final String businessImpact;
    private final String suggestedResolution;
    private final Map<String, Object> supportingMetrics;
    private final String source;
    private final String traceId;
    private final long executionTimeMs;
    private final Instant createdAt;
    private final Instant acknowledgedAt;
    private final Instant resolvedAt;

    public Alert(String id, String title, String description, AlertCategory category,
                 AlertSeverity severity, AlertPriority priority, String domain, AlertStatus status,
                 String businessImpact, String suggestedResolution, Map<String, Object> supportingMetrics,
                 String source, String traceId, long executionTimeMs, Instant createdAt,
                 Instant acknowledgedAt, Instant resolvedAt) {
        this.id = id; this.title = title; this.description = description; this.category = category;
        this.severity = severity; this.priority = priority; this.domain = domain; this.status = status;
        this.businessImpact = businessImpact; this.suggestedResolution = suggestedResolution;
        this.supportingMetrics = supportingMetrics; this.source = source; this.traceId = traceId;
        this.executionTimeMs = executionTimeMs; this.createdAt = createdAt;
        this.acknowledgedAt = acknowledgedAt; this.resolvedAt = resolvedAt;
    }

    public static Alert create(String title, String description, AlertCategory category,
                                AlertSeverity severity, AlertPriority priority, String domain,
                                String businessImpact, String suggestedResolution,
                                Map<String, Object> supportingMetrics) {
        return new Alert(UUID.randomUUID().toString(), title, description, category, severity,
                priority, domain, AlertStatus.OPEN, businessImpact, suggestedResolution,
                supportingMetrics, "alert-engine", UUID.randomUUID().toString(), 8,
                Instant.now(), null, null);
    }

    public String id() { return id; }
    public String title() { return title; }
    public String description() { return description; }
    public AlertCategory category() { return category; }
    public AlertSeverity severity() { return severity; }
    public AlertPriority priority() { return priority; }
    public String domain() { return domain; }
    public AlertStatus status() { return status; }
    public String businessImpact() { return businessImpact; }
    public String suggestedResolution() { return suggestedResolution; }
    public Map<String, Object> supportingMetrics() { return supportingMetrics; }
    public String source() { return source; }
    public String traceId() { return traceId; }
    public long executionTimeMs() { return executionTimeMs; }
    public Instant createdAt() { return createdAt; }
    public Instant acknowledgedAt() { return acknowledgedAt; }
    public Instant resolvedAt() { return resolvedAt; }

    public Alert acknowledge() {
        return new Alert(id, title, description, category, severity, priority, domain,
                AlertStatus.ACKNOWLEDGED, businessImpact, suggestedResolution, supportingMetrics,
                source, traceId, executionTimeMs, createdAt, Instant.now(), resolvedAt);
    }

    public Alert resolve() {
        return new Alert(id, title, description, category, severity, priority, domain,
                AlertStatus.RESOLVED, businessImpact, suggestedResolution, supportingMetrics,
                source, traceId, executionTimeMs, createdAt, acknowledgedAt, Instant.now());
    }
}
