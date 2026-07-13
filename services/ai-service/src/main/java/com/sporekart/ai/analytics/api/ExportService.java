package com.sporekart.ai.analytics.api;

import com.sporekart.ai.analytics.domain.GovernanceExport;
import com.sporekart.ai.analytics.domain.ReportFormat;
import java.util.List;
import java.util.UUID;

public interface ExportService {
    GovernanceExport exportReport(UUID reportId, ReportFormat format);
    GovernanceExport getExport(UUID id);
    List<GovernanceExport> getExportsByReport(UUID reportId);
    byte[] getExportData(UUID id);
}
