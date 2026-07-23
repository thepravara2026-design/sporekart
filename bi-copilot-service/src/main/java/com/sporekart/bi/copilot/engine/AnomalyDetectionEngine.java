package com.sporekart.bi.copilot.engine;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sporekart.bi.copilot.domain.AnomalyAlert;

@Component
public class AnomalyDetectionEngine {

    private static final Logger log = LoggerFactory.getLogger(AnomalyDetectionEngine.class);
    private static final Random RANDOM = new Random(505);

    private static final double Z_SCORE_THRESHOLD_ANOMALY = 2.5;
    private static final double Z_SCORE_THRESHOLD_CRITICAL = 3.5;

    private final Map<String, AnomalyAlert> activeAnomalies = new ConcurrentHashMap<>();
    private final Map<String, List<Double>> metricHistory = new ConcurrentHashMap<>();

    public AnomalyDetectionEngine() {
        log.info("AnomalyDetectionEngine initialized with z-score thresholds: anomaly={}, critical={}",
            Z_SCORE_THRESHOLD_ANOMALY, Z_SCORE_THRESHOLD_CRITICAL);
        seedActiveAnomalies();
    }

    public List<AnomalyAlert> detectAnomalies(String metric, List<Double> dataPoints) {
        if (dataPoints == null || dataPoints.size() < 3) return List.of();

        List<Double> history = metricHistory.computeIfAbsent(metric, k -> new ArrayList<>());
        history.addAll(dataPoints);
        if (history.size() > 50) {
            history.subList(0, history.size() - 50).clear();
        }

        List<AnomalyAlert> alerts = new ArrayList<>();
        double mean = history.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double variance = history.stream()
            .mapToDouble(v -> Math.pow(v - mean, 2))
            .average().orElse(0);
        double stdDev = Math.sqrt(variance);
        if (stdDev == 0) return List.of();

        for (Double value : dataPoints) {
            double zScore = Math.abs((value - mean) / stdDev);

            if (zScore > Z_SCORE_THRESHOLD_CRITICAL) {
                AnomalyAlert alert = createAlert(metric, "critical", value, mean, stdDev, zScore);
                alerts.add(alert);
                activeAnomalies.put(alert.anomalyId(), alert);
                log.warn("Critical anomaly detected for metric '{}': value={}, expected={}, z-score={}",
                    metric, value, mean, zScore);
            } else if (zScore > Z_SCORE_THRESHOLD_ANOMALY) {
                AnomalyAlert alert = createAlert(metric, "anomaly", value, mean, stdDev, zScore);
                alerts.add(alert);
                activeAnomalies.put(alert.anomalyId(), alert);
                log.info("Anomaly detected for metric '{}': value={}, expected={}, z-score={}",
                    metric, value, mean, zScore);
            }
        }

        return alerts;
    }

    public List<AnomalyAlert> detectAnomaliesAcrossMetrics() {
        List<AnomalyAlert> allAlerts = new ArrayList<>();

        String[] defaultMetrics = {"revenue", "orders", "active_customers", "yield", "satisfaction"};
        for (String metric : defaultMetrics) {
            List<Double> sampleData = generateSampleData(metric);
            List<AnomalyAlert> alerts = detectAnomalies(metric, sampleData);
            allAlerts.addAll(alerts);
        }

        return allAlerts;
    }

    public double calculateDeviationScore(double observed, double expected, double stdDev) {
        if (stdDev == 0) return 0;
        return Math.abs((observed - expected) / stdDev);
    }

    public List<AnomalyAlert> getActiveAnomalies() {
        return List.copyOf(activeAnomalies.values());
    }

    public void resolveAnomaly(String anomalyId) {
        AnomalyAlert alert = activeAnomalies.remove(anomalyId);
        if (alert != null) {
            log.info("Resolved anomaly: {} ({})", anomalyId, alert.metric());
        } else {
            log.warn("Anomaly not found for resolution: {}", anomalyId);
        }
    }

    public Map<String, Object> getAnomalySummary() {
        List<AnomalyAlert> all = getActiveAnomalies();

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalAnomalies", all.size());

        Map<String, Long> bySeverity = all.stream()
            .collect(Collectors.groupingBy(AnomalyAlert::severity, Collectors.counting()));
        summary.put("bySeverity", bySeverity);

        Map<String, Long> byMetric = all.stream()
            .collect(Collectors.groupingBy(AnomalyAlert::metric, Collectors.counting()));
        summary.put("byMetric", byMetric);

        summary.put("criticalCount", bySeverity.getOrDefault("critical", 0L));
        summary.put("anomalyCount", bySeverity.getOrDefault("anomaly", 0L));

        return summary;
    }

    public int autoResolveAnomalies(String metric) {
        List<Double> history = metricHistory.get(metric);
        if (history == null || history.size() < 3) return 0;

        int resolved = 0;
        double mean = history.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double variance = history.stream()
            .mapToDouble(v -> Math.pow(v - mean, 2))
            .average().orElse(0);
        double stdDev = Math.sqrt(variance);

        List<AnomalyAlert> toRemove = new ArrayList<>();
        for (AnomalyAlert alert : activeAnomalies.values()) {
            if (!alert.metric().equals(metric) || alert.autoResolved()) continue;

            List<Double> recentHistory = history.subList(
                Math.max(0, history.size() - 5), history.size());
            boolean belowThreshold = recentHistory.stream()
                .allMatch(v -> Math.abs((v - mean) / Math.max(stdDev, 0.001)) < Z_SCORE_THRESHOLD_ANOMALY);

            if (belowThreshold) {
                toRemove.add(alert);
            }
        }

        for (AnomalyAlert alert : toRemove) {
            activeAnomalies.remove(alert.anomalyId());
            resolved++;
            log.info("Auto-resolved anomaly: {} for metric {}", alert.anomalyId(), metric);
        }

        return resolved;
    }

    private AnomalyAlert createAlert(String metric, String severity, double value, double mean, double stdDev, double zScore) {
        String dimension = deriveDimension(metric);
        String description = String.format(
            "%s value %.2f deviates from expected value %.2f (z-score: %.2f)",
            metric, value, mean, zScore
        );
        String impact = severity.equals("critical") ? "high" : "medium";
        String action = severity.equals("critical")
            ? "Investigate immediately and take corrective action"
            : "Monitor and review within 24 hours";

        return new AnomalyAlert(
            UUID.randomUUID().toString(),
            metric,
            dimension,
            Math.round(value * 100) / 100.0,
            Math.round(mean * 100) / 100.0,
            Math.round(zScore * 100) / 100.0,
            severity,
            description,
            impact,
            action,
            false,
            OffsetDateTime.now(),
            null
        );
    }

    private String deriveDimension(String metric) {
        return switch (metric) {
            case "revenue" -> "overall";
            case "orders" -> "channel";
            case "active_customers" -> "segment";
            case "yield" -> "species";
            case "satisfaction" -> "customer";
            default -> "general";
        };
    }

    private List<Double> generateSampleData(String metric) {
        List<Double> data = new ArrayList<>();
        double base = switch (metric) {
            case "revenue" -> 400000;
            case "orders" -> 2000;
            case "active_customers" -> 1200;
            case "yield" -> 3000;
            case "satisfaction" -> 4.4;
            default -> 100;
        };
        double noise = base * 0.05;

        for (int i = 0; i < 6; i++) {
            double value = base + (i - 3) * base * 0.02 + RANDOM.nextDouble() * noise - noise / 2;
            data.add(Math.round(value * 100) / 100.0);
        }

        if (RANDOM.nextDouble() < 0.3) {
            int spikeIndex = RANDOM.nextInt(data.size());
            data.set(spikeIndex, data.get(spikeIndex) * (1.3 + RANDOM.nextDouble() * 0.4));
        }

        return data;
    }

    private void seedActiveAnomalies() {
        String[][] seedAnomalies = {
            {"revenue", "2025-10", String.valueOf(680000), String.valueOf(520000), String.valueOf(3.8), "critical"},
            {"orders", "2025-11", String.valueOf(3200), String.valueOf(2400), String.valueOf(2.9), "anomaly"}
        };

        for (String[] parts : seedAnomalies) {
            AnomalyAlert alert = new AnomalyAlert(
                UUID.randomUUID().toString(),
                parts[0],
                deriveDimension(parts[0]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]),
                Double.parseDouble(parts[4]),
                parts[5],
                String.format("%s spike detected in %s", parts[0], parts[1]),
                parts[5].equals("critical") ? "high" : "medium",
                "Review and investigate cause of spike",
                false,
                OffsetDateTime.now().minusDays(1),
                null
            );
            activeAnomalies.put(alert.anomalyId(), alert);
        }

        log.info("Seeded {} active anomalies", activeAnomalies.size());
    }
}
