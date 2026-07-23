package com.sporekart.executive.copilot.dto;

import java.util.List;
import java.util.Map;

public record ReportResponse(
    String reportId,
    String title,
    String period,
    String executiveSummary,
    Map<String, Object> financialSummary,
    Map<String, Object> businessKPIs,
    List<RiskItemBrief> strategicRisks,
    List<OpportunityItem> growthOpportunities,
    Map<String, String> departmentalStatus,
    String futureOutlook
) {
    public record RiskItemBrief(String title, String category, double score, String severity) {}
    public record OpportunityItem(String title, String description, double roi, double confidence) {}
}
