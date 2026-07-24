package com.sporekart.analytics.infrastructure.persistence;

import com.sporekart.analytics.domain.model.ReportRequest;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "report_requests")
public class ReportRequestEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "report_type", nullable = false, length = 100)
    private String reportType;

    @Column(name = "format", nullable = false, length = 30)
    private String format;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected ReportRequestEntity() {}

    public ReportRequestEntity(String id, String reportType, String format, Instant createdAt) {
        this.id = id;
        this.reportType = reportType;
        this.format = format;
        this.createdAt = createdAt;
    }

    public static ReportRequestEntity fromDomain(ReportRequest request) {
        return new ReportRequestEntity(request.getId(), request.getReportType(), request.getFormat(), request.getCreatedAt());
    }

    public ReportRequest toDomain() {
        return new ReportRequest(id, reportType, format, createdAt);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
