package com.sporekart.alert.application.sdk;

import com.sporekart.alert.domain.model.*;

import java.util.*;

public final class AlertBuilder {

    private String title;
    private String description;
    private AlertCategory category = AlertCategory.OPERATIONAL;
    private AlertSeverity severity = AlertSeverity.MEDIUM;
    private AlertPriority priority = AlertPriority.P3;
    private String domain;
    private String businessImpact;
    private String suggestedResolution;
    private Map<String, Object> supportingMetrics = new LinkedHashMap<>();

    public AlertBuilder withTitle(String title) { this.title = title; return this; }
    public AlertBuilder withDescription(String description) { this.description = description; return this; }
    public AlertBuilder withCategory(AlertCategory category) { this.category = category; return this; }
    public AlertBuilder withSeverity(AlertSeverity severity) { this.severity = severity; return this; }
    public AlertBuilder withPriority(AlertPriority priority) { this.priority = priority; return this; }
    public AlertBuilder withDomain(String domain) { this.domain = domain; return this; }
    public AlertBuilder withBusinessImpact(String businessImpact) { this.businessImpact = businessImpact; return this; }
    public AlertBuilder withSuggestedResolution(String suggestedResolution) { this.suggestedResolution = suggestedResolution; return this; }
    public AlertBuilder withSupportingMetrics(Map<String, Object> supportingMetrics) { this.supportingMetrics = supportingMetrics; return this; }
    public AlertBuilder addMetric(String key, Object value) { this.supportingMetrics.put(key, value); return this; }

    public Alert build() {
        if (title == null || title.isBlank()) throw new IllegalStateException("title is required");
        if (domain == null || domain.isBlank()) throw new IllegalStateException("domain is required");
        return Alert.create(title, description, category, severity, priority,
                domain, businessImpact, suggestedResolution, supportingMetrics);
    }

    public static AlertBuilder builder() { return new AlertBuilder(); }
}
