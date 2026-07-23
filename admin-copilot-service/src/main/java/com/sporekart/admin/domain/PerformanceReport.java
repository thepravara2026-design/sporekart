package com.sporekart.admin.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record PerformanceReport(
    String id,
    String title,
    ReportType type,
    OffsetDateTime generatedAt,
    String period,
    Map<String, Object> metrics,
    List<String> charts,
    Map<String, String> filters,
    String downloadUrl
) {

    public enum ReportType {
        PDF, EXCEL, CSV
    }
}
