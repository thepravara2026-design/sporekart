package com.sporekart.ai.analytics.api;

import com.sporekart.ai.analytics.domain.GovernanceReport;
import com.sporekart.ai.analytics.domain.ReportType;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface GovernanceReportingService {
    GovernanceReport generateReport(ReportType type, Map<String, Object> params);
    GovernanceReport getReport(UUID id);
    List<GovernanceReport> getReportsByType(ReportType type);
    List<GovernanceReport> getReportsByDateRange(Instant from, Instant to);
    GovernanceReport regenerateReport(UUID id);
}
