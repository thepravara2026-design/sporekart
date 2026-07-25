package com.sporekart.alert.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Anomaly {
    private final String id;
    private final AnomalyType type;
    private final String domain;
    private final AlertSeverity severity;
    private final String description;
    private final double expectedValue;
    private final double actualValue;
    private final double deviation;
    private final double confidence;
    private final Instant detectedAt;

    public Anomaly(String id, AnomalyType type, String domain, AlertSeverity severity,
                   String description, double expectedValue, double actualValue,
                   double deviation, double confidence, Instant detectedAt) {
        this.id = id; this.type = type; this.domain = domain; this.severity = severity;
        this.description = description; this.expectedValue = expectedValue;
        this.actualValue = actualValue; this.deviation = deviation;
        this.confidence = confidence; this.detectedAt = detectedAt;
    }

    public static Anomaly create(AnomalyType type, String domain, AlertSeverity severity,
                                  String description, double expectedValue, double actualValue,
                                  double deviation, double confidence) {
        return new Anomaly(UUID.randomUUID().toString(), type, domain, severity, description,
                expectedValue, actualValue, deviation, confidence, Instant.now());
    }

    public String id() { return id; }
    public AnomalyType type() { return type; }
    public String domain() { return domain; }
    public AlertSeverity severity() { return severity; }
    public String description() { return description; }
    public double expectedValue() { return expectedValue; }
    public double actualValue() { return actualValue; }
    public double deviation() { return deviation; }
    public double confidence() { return confidence; }
    public Instant detectedAt() { return detectedAt; }
}
