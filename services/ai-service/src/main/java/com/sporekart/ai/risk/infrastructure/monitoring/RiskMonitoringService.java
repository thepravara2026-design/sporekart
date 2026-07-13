package com.sporekart.ai.risk.infrastructure.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@Service
@Slf4j
public class RiskMonitoringService {

    private final MeterRegistry meterRegistry;

    private Counter totalAssessments;
    private Counter highRiskCount;
    private Counter criticalRiskCount;
    private Counter recommendationsGenerated;
    private Timer assessmentLatency;
    private Timer scoringLatency;

    private final AtomicReference<Double> averageScoreRef = new AtomicReference<>(0.0);
    private final AtomicReference<Double> trustAverageRef = new AtomicReference<>(0.0);
    private final AtomicReference<Double> confidenceAverageRef = new AtomicReference<>(0.0);

    public RiskMonitoringService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        totalAssessments = Counter.builder("risk.assessments.total")
            .description("Total risk assessments")
            .register(meterRegistry);

        highRiskCount = Counter.builder("risk.high.count")
            .description("High risk assessments count")
            .register(meterRegistry);

        criticalRiskCount = Counter.builder("risk.critical.count")
            .description("Critical risk assessments count")
            .register(meterRegistry);

        recommendationsGenerated = Counter.builder("risk.recommendations")
            .description("Generated risk recommendations")
            .register(meterRegistry);

        assessmentLatency = Timer.builder("risk.assessment.latency")
            .description("Risk assessment latency")
            .register(meterRegistry);

        scoringLatency = Timer.builder("risk.scoring.latency")
            .description("Risk scoring latency")
            .register(meterRegistry);

        Gauge.builder("risk.average.score", averageScoreRef, AtomicReference::get)
            .description("Average risk score")
            .register(meterRegistry);

        Gauge.builder("trust.average.score", trustAverageRef, AtomicReference::get)
            .description("Average trust score")
            .register(meterRegistry);

        Gauge.builder("confidence.average.score", confidenceAverageRef, AtomicReference::get)
            .description("Average confidence score")
            .register(meterRegistry);

        log.info("Risk monitoring service initialized");
    }

    public void recordRequest() {
        totalAssessments.increment();
    }

    public void recordAssessment(long latencyMs) {
        assessmentLatency.record(latencyMs, TimeUnit.MILLISECONDS);
    }

    public void recordRecalculation(long latencyMs) {
        assessmentLatency.record(latencyMs, TimeUnit.MILLISECONDS);
    }

    public void recordScoring(long latencyMs) {
        scoringLatency.record(latencyMs, TimeUnit.MILLISECONDS);
    }

    public void recordRiskLevel(String level) {
        if ("HIGH".equals(level)) {
            highRiskCount.increment();
        } else if ("CRITICAL".equals(level)) {
            criticalRiskCount.increment();
        }
    }

    public void recordRecommendation() {
        recommendationsGenerated.increment();
    }

    public void updateAverageScore(double score) {
        averageScoreRef.set(score);
    }

    public void updateTrustAverage(double score) {
        trustAverageRef.set(score);
    }

    public void updateConfidenceAverage(double score) {
        confidenceAverageRef.set(score);
    }

    public Map<String, Object> checkHealth() {
        return Map.of(
            "status", "UP",
            "service", "risk",
            "timestamp", System.currentTimeMillis(),
            "details", Map.of(
                "totalAssessments", totalAssessments.count(),
                "highRiskCount", highRiskCount.count(),
                "criticalRiskCount", criticalRiskCount.count()
            )
        );
    }
}
