package com.sporekart.report.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ReportTemplate(
    String id,
    String name,
    String description,
    ReportCategory category,
    ReportType type,
    String owner,
    List<String> sections,
    Map<String, Object> defaultConfig,
    boolean active,
    Instant createdAt,
    Instant updatedAt
) {
    public static ReportTemplate create(
        String name, String description, ReportCategory category, ReportType type,
        String owner, List<String> sections, Map<String, Object> defaultConfig
    ) {
        return new ReportTemplate(
            UUID.randomUUID().toString(),
            name, description, category, type, owner,
            sections != null ? List.copyOf(sections) : List.of(),
            defaultConfig != null ? Map.copyOf(defaultConfig) : Map.of(),
            true, Instant.now(), Instant.now()
        );
    }

    public ReportTemplate withActive(boolean active) {
        return new ReportTemplate(id, name, description, category, type, owner,
            sections, defaultConfig, active, createdAt, Instant.now());
    }
}
