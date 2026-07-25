package com.sporekart.alert.domain.model;

import java.time.Instant;
import java.util.UUID;

public class BusinessRisk {
    private final String id;
    private final String title;
    private final String description;
    private final RiskCategory category;
    private final RiskSeverity severity;
    private final String domain;
    private final String impact;
    private final double likelihood;
    private final double riskScore;
    private final String mitigationSteps;
    private final String status;
    private final Instant createdAt;

    public BusinessRisk(String id, String title, String description, RiskCategory category,
                        RiskSeverity severity, String domain, String impact, double likelihood,
                        double riskScore, String mitigationSteps, String status, Instant createdAt) {
        this.id = id; this.title = title; this.description = description; this.category = category;
        this.severity = severity; this.domain = domain; this.impact = impact; this.likelihood = likelihood;
        this.riskScore = riskScore; this.mitigationSteps = mitigationSteps; this.status = status;
        this.createdAt = createdAt;
    }

    public static BusinessRisk create(String title, String description, RiskCategory category,
                                       RiskSeverity severity, String domain, String impact,
                                       double likelihood, double riskScore, String mitigationSteps) {
        return new BusinessRisk(UUID.randomUUID().toString(), title, description, category, severity,
                domain, impact, likelihood, riskScore, mitigationSteps, "ACTIVE", Instant.now());
    }

    public String id() { return id; }
    public String title() { return title; }
    public String description() { return description; }
    public RiskCategory category() { return category; }
    public RiskSeverity severity() { return severity; }
    public String domain() { return domain; }
    public String impact() { return impact; }
    public double likelihood() { return likelihood; }
    public double riskScore() { return riskScore; }
    public String mitigationSteps() { return mitigationSteps; }
    public String status() { return status; }
    public Instant createdAt() { return createdAt; }
}
