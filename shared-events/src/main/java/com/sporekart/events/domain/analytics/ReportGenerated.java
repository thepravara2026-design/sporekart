package com.sporekart.events.domain.analytics;

import com.sporekart.events.model.DomainEvent;

public class ReportGenerated extends DomainEvent {
    private final String reportId;
    private final String reportType;

    private ReportGenerated(Builder builder) {
        super(builder);
        this.reportId = builder.reportId;
        this.reportType = builder.reportType;
    }

    public String getReportId() { return reportId; }
    public String getReportType() { return reportType; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String reportId;
        private String reportType;

        public Builder reportId(String reportId) { this.reportId = reportId; return this; }
        public Builder reportType(String reportType) { this.reportType = reportType; return this; }

        public ReportGenerated build() {
            return new ReportGenerated(this);
        }
    }
}
