package com.sporekart.report.infrastructure.export;

import com.sporekart.report.domain.model.ExportFormat;
import com.sporekart.report.domain.model.Report;
import com.sporekart.report.domain.model.ReportExport;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class MockExportService {
    private final ReportRepositoryPort repository;

    public MockExportService(ReportRepositoryPort repository) {
        this.repository = repository;
    }

    public ReportExport exportReport(String reportId, ExportFormat format) {
        Report report = repository.findReportById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("Report not found: " + reportId));
        String filename = ReportExport.generateFilename(report.title(), format);
        long estimatedSize = estimateFileSize(report, format);
        ReportExport export = ReportExport.create(reportId, report.title(), format, filename, estimatedSize);
        return repository.saveExport(export);
    }

    public ReportExport getExportStatus(String exportId) {
        return repository.findExportById(exportId)
            .orElseThrow(() -> new IllegalArgumentException("Export not found: " + exportId));
    }

    public java.util.List<ReportExport> getExportHistory(String reportId) {
        return repository.findExportsByReportId(reportId);
    }

    public java.util.List<ReportExport> getAllExports() {
        return repository.findAllExports();
    }

    private long estimateFileSize(Report report, ExportFormat format) {
        int contentSize = report.title().length() + report.summary().length()
            + report.kpis().toString().length() + report.recommendations().toString().length();
        return switch (format) {
            case PDF -> contentSize * 10L;
            case EXCEL -> contentSize * 5L;
            case CSV -> contentSize * 2L;
            case JSON -> contentSize * 3L;
        };
    }
}
