package com.sporekart.report.domain.model;

import java.time.Instant;
import java.util.UUID;

public record ReportExport(
    String id,
    String reportId,
    String reportTitle,
    ExportFormat format,
    String filename,
    long fileSizeBytes,
    String status,
    Instant exportedAt,
    Instant createdAt
) {
    public static ReportExport create(
        String reportId, String reportTitle, ExportFormat format,
        String filename, long fileSizeBytes
    ) {
        return new ReportExport(
            UUID.randomUUID().toString(),
            reportId, reportTitle, format, filename, fileSizeBytes,
            "COMPLETED", Instant.now(), Instant.now()
        );
    }

    public static String generateFilename(String reportTitle, ExportFormat format) {
        return reportTitle.toLowerCase().replaceAll("\\s+", "-")
            + "-" + Instant.now().toEpochMilli()
            + switch (format) {
                case PDF -> ".pdf";
                case EXCEL -> ".xlsx";
                case CSV -> ".csv";
                case JSON -> ".json";
            };
    }
}
