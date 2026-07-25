package com.sporekart.report.application.sdk;

import com.sporekart.report.domain.model.ExportFormat;
import com.sporekart.report.domain.model.ReportExport;
import com.sporekart.report.infrastructure.export.MockExportService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ExportRuntime {
    private final MockExportService exportService;

    public ExportRuntime(MockExportService exportService) {
        this.exportService = exportService;
    }

    public ReportExport exportReport(String reportId, ExportFormat format) {
        return exportService.exportReport(reportId, format);
    }

    public ReportExport getExportStatus(String exportId) {
        return exportService.getExportStatus(exportId);
    }

    public List<ReportExport> getExportHistory(String reportId) {
        return exportService.getExportHistory(reportId);
    }

    public List<ReportExport> getAllExports() {
        return exportService.getAllExports();
    }
}
