package com.sporekart.analytics.domain.model;

import java.time.Instant;
import java.util.UUID;

public class ReportRequest {
    private final String id;
    private final String reportType;
    private final String format;
    private final Instant createdAt;

    public ReportRequest(String id, String reportType, String format, Instant createdAt) {
        this.id = id;
        this.reportType = reportType;
        this.format = format;
        this.createdAt = createdAt;
    }

    public static ReportRequest create(String reportType, String format) {
        return new ReportRequest(UUID.randomUUID().toString(), reportType, format, Instant.now());
    }

    public String getId() {
        return id;
    }

    public String getReportType() {
        return reportType;
    }

    public String getFormat() {
        return format;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
