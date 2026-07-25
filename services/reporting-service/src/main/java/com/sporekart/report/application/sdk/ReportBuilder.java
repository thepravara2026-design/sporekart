package com.sporekart.report.application.sdk;

import com.sporekart.report.domain.model.*;
import java.util.*;

public final class ReportBuilder {
    private String title;
    private String description;
    private ReportType type = ReportType.EXECUTIVE;
    private ReportCategory category = ReportCategory.EXECUTIVE;
    private String owner;
    private String summary;
    private String businessHealth;
    private List<String> recommendations = new ArrayList<>();
    private List<String> risks = new ArrayList<>();
    private Map<String, Object> kpis = new LinkedHashMap<>();
    private Map<String, Object> supportingMetrics = new LinkedHashMap<>();
    private String templateId;

    private ReportBuilder() {}

    public static ReportBuilder builder() { return new ReportBuilder(); }

    public ReportBuilder withTitle(String title) { this.title = title; return this; }
    public ReportBuilder withDescription(String description) { this.description = description; return this; }
    public ReportBuilder withType(ReportType type) { this.type = type; return this; }
    public ReportBuilder withCategory(ReportCategory category) { this.category = category; return this; }
    public ReportBuilder withOwner(String owner) { this.owner = owner; return this; }
    public ReportBuilder withSummary(String summary) { this.summary = summary; return this; }
    public ReportBuilder withBusinessHealth(String businessHealth) { this.businessHealth = businessHealth; return this; }
    public ReportBuilder withRecommendations(List<String> recommendations) { this.recommendations = recommendations; return this; }
    public ReportBuilder addRecommendation(String recommendation) { this.recommendations.add(recommendation); return this; }
    public ReportBuilder withRisks(List<String> risks) { this.risks = risks; return this; }
    public ReportBuilder addRisk(String risk) { this.risks.add(risk); return this; }
    public ReportBuilder withKpis(Map<String, Object> kpis) { this.kpis = kpis; return this; }
    public ReportBuilder addKpi(String key, Object value) { this.kpis.put(key, value); return this; }
    public ReportBuilder withSupportingMetrics(Map<String, Object> metrics) { this.supportingMetrics = metrics; return this; }
    public ReportBuilder addMetric(String key, Object value) { this.supportingMetrics.put(key, value); return this; }
    public ReportBuilder withTemplateId(String templateId) { this.templateId = templateId; return this; }

    public Report build() {
        if (title == null || title.isBlank()) throw new IllegalStateException("title is required");
        if (owner == null || owner.isBlank()) throw new IllegalStateException("owner is required");
        return Report.create(title, description, type, category, owner,
            summary, businessHealth, recommendations, risks, kpis, supportingMetrics, templateId);
    }
}
