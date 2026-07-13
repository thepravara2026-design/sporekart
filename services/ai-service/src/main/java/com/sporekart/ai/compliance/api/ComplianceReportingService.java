package com.sporekart.ai.compliance.api;

import com.sporekart.ai.compliance.domain.*;
import java.util.List;
import java.util.UUID;

public interface ComplianceReportingService {
    ComplianceReport generateReport(UUID frameworkId, String title);
    ComplianceReport generateReport(UUID frameworkId, String title, UUID generatedBy);
    List<ComplianceReport> getReportsByFramework(UUID frameworkId);
    ComplianceReport getReport(UUID id);
}
