package com.sporekart.executive.copilot.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record BoardReport(
    String reportId,
    String title,
    String period,
    LocalDate generatedAt,
    String executiveSummary,
    Map<String, Object> financialSummary,
    Map<String, Object> businessKPIs,
    List<BusinessRisk> strategicRisks,
    List<StrategicRecommendation> growthOpportunities,
    Map<String, String> departmentalStatus,
    String futureOutlook
) {}
