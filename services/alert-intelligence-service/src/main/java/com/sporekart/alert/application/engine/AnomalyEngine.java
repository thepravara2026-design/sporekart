package com.sporekart.alert.application.engine;

import com.sporekart.alert.domain.model.*;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnomalyEngine {

    private final AlertRepositoryPort repository;

    public AnomalyEngine(AlertRepositoryPort repository) { this.repository = repository; }

    public List<Anomaly> detectAllAnomalies() {
        List<Anomaly> anomalies = new ArrayList<>();
        anomalies.addAll(detectGrowthAnomalies());
        anomalies.addAll(detectDeclineAnomalies());
        anomalies.addAll(detectOutlierAnomalies());
        anomalies.addAll(detectTrendBreakAnomalies());
        anomalies.addAll(detectSeasonalityAnomalies());
        anomalies.addAll(detectVolumeSpikeAnomalies());
        anomalies.addAll(detectPerformanceAnomalies());
        anomalies.addAll(detectFailureAnomalies());
        return anomalies;
    }

    public List<Anomaly> detectAnomaliesByType(AnomalyType type) {
        return switch (type) {
            case UNEXPECTED_GROWTH -> detectGrowthAnomalies();
            case UNEXPECTED_DECLINE -> detectDeclineAnomalies();
            case OUTLIER -> detectOutlierAnomalies();
            case TREND_BREAK -> detectTrendBreakAnomalies();
            case SEASONALITY_CHANGE -> detectSeasonalityAnomalies();
            case VOLUME_SPIKE -> detectVolumeSpikeAnomalies();
            case PERFORMANCE_DROP -> detectPerformanceAnomalies();
            case OPERATIONAL_FAILURE -> detectFailureAnomalies();
        };
    }

    public List<Anomaly> detectAnomaliesForDomain(String domain) {
        return detectAllAnomalies().stream().filter(a -> a.domain().equals(domain)).toList();
    }

    private List<Anomaly> detectGrowthAnomalies() {
        return List.of(anomaly(AnomalyType.UNEXPECTED_GROWTH, "REVENUE", AlertSeverity.INFO,
                "Revenue growing 35% faster than forecast", 250000, 337500, 35.0, 92.0));
    }

    private List<Anomaly> detectDeclineAnomalies() {
        return List.of(anomaly(AnomalyType.UNEXPECTED_DECLINE, "ORDERS", AlertSeverity.HIGH,
                "Order volume declined 20% week-over-week", 1450, 1160, 20.0, 89.0));
    }

    private List<Anomaly> detectOutlierAnomalies() {
        return List.of(anomaly(AnomalyType.OUTLIER, "INVENTORY", AlertSeverity.MEDIUM,
                "Inventory turnover rate 3x above normal for 2 SKUs", 4.5, 13.7, 204.0, 85.0));
    }

    private List<Anomaly> detectTrendBreakAnomalies() {
        return List.of(anomaly(AnomalyType.TREND_BREAK, "CUSTOMERS", AlertSeverity.MEDIUM,
                "New customer acquisition trend broken after 6 months of growth", 320, 208, 35.0, 91.0));
    }

    private List<Anomaly> detectSeasonalityAnomalies() {
        return List.of(anomaly(AnomalyType.SEASONALITY_CHANGE, "MARKETPLACE", AlertSeverity.LOW,
                "Marketplace demand pattern shifting 2 weeks earlier than seasonal norm", 100, 132, 32.0, 78.0));
    }

    private List<Anomaly> detectVolumeSpikeAnomalies() {
        return List.of(anomaly(AnomalyType.VOLUME_SPIKE, "TRAINING", AlertSeverity.INFO,
                "Course enrollment spike — 280% above daily average", 45, 171, 280.0, 95.0));
    }

    private List<Anomaly> detectPerformanceAnomalies() {
        return List.of(anomaly(AnomalyType.PERFORMANCE_DROP, "PLATFORM", AlertSeverity.HIGH,
                "API P99 latency degraded from 500ms to 1200ms", 500, 1200, 140.0, 93.0));
    }

    private List<Anomaly> detectFailureAnomalies() {
        return List.of(anomaly(AnomalyType.OPERATIONAL_FAILURE, "WORKFLOW", AlertSeverity.CRITICAL,
                "Payment processing workflow failing for 8% of transactions", 0.5, 8.0, 1500.0, 96.0));
    }

    private Anomaly anomaly(AnomalyType type, String domain, AlertSeverity severity,
                             String description, double expected, double actual,
                             double deviation, double confidence) {
        Anomaly a = Anomaly.create(type, domain, severity, description, expected, actual, deviation, confidence);
        return repository.saveAnomaly(a);
    }
}
