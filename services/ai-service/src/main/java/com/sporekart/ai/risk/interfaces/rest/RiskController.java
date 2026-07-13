package com.sporekart.ai.risk.interfaces.rest;

import com.sporekart.ai.risk.api.RiskEngine;
import com.sporekart.ai.risk.api.RiskMetricsService;
import com.sporekart.ai.risk.api.RiskConfigurationService;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.infrastructure.kafka.RiskKafkaEventPublisher;
import com.sporekart.ai.risk.infrastructure.monitoring.RiskMonitoringService;
import com.sporekart.ai.risk.infrastructure.persistence.RiskHistoryRepository;
import com.sporekart.ai.risk.infrastructure.persistence.TrustScoreRepository;
import com.sporekart.ai.risk.infrastructure.persistence.ConfidenceScoreRepository;
import com.sporekart.ai.risk.interfaces.rest.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/v1/risk")
@RequiredArgsConstructor
public class RiskController {

    private final RiskEngine riskEngine;
    private final RiskMetricsService riskMetricsService;
    private final RiskHistoryRepository riskHistoryRepository;
    private final RiskConfigurationService riskConfigurationService;
    private final TrustScoreRepository trustScoreRepository;
    private final ConfidenceScoreRepository confidenceScoreRepository;
    private final RiskKafkaEventPublisher riskKafkaEventPublisher;
    private final RiskMonitoringService riskMonitoringService;

    @PostMapping("/assess")
    public ResponseEntity<RiskAssessResponse> assess(@RequestBody RiskAssessRequest request) {
        riskMonitoringService.recordRequest();
        long start = System.currentTimeMillis();

        var domainRequest = new RiskAssessment(
            UUID.randomUUID(),
            request.module(),
            request.action(),
            RiskAssessmentStatus.PENDING,
            request.context() != null ? request.context() : new HashMap<>(),
            null,
            Instant.now(),
            null
        );

        var result = riskEngine.assess(domainRequest);

        long elapsed = System.currentTimeMillis() - start;
        riskMonitoringService.recordAssessment(elapsed);
        riskMonitoringService.recordRiskLevel(result.riskLevel().name());

        riskKafkaEventPublisher.publishAssessmentCompleted(
            result.id().toString(),
            result.riskLevel().name(),
            elapsed
        );

        return ResponseEntity.ok(new RiskAssessResponse(
            result.id().toString(),
            result.riskLevel() == RiskLevel.LOW || result.riskLevel() == RiskLevel.MEDIUM,
            result.riskLevel().name(),
            result.overallScore(),
            0.0,
            0.0,
            "Assessment completed",
            "Risk assessment processed successfully",
            System.currentTimeMillis()
        ));
    }

    @GetMapping("/history")
    public ResponseEntity<RiskHistoryDto> getHistory() {
        var entries = riskHistoryRepository.findAll().stream()
            .map(h -> new HistoryEntryDto(
                h.id().toString(),
                h.assessmentId().toString(),
                h.eventType(),
                h.description(),
                h.timestamp().toString()
            ))
            .toList();
        return ResponseEntity.ok(new RiskHistoryDto(entries, entries.size()));
    }

    @GetMapping("/statistics")
    public ResponseEntity<RiskStatsDto> getStatistics() {
        var stats = riskMetricsService.getStatistics();
        return ResponseEntity.ok(new RiskStatsDto(
            stats.totalAssessments(),
            stats.averageRiskScore(),
            stats.riskDistribution(),
            stats.trustTrends(),
            stats.confidenceTrends(),
            stats.recommendationCounts()
        ));
    }

    @GetMapping("/thresholds")
    public ResponseEntity<List<ThresholdDto>> getThresholds() {
        var thresholds = riskConfigurationService.getThresholds().stream()
            .map(t -> new ThresholdDto(
                t.level().name(),
                t.minScore(),
                t.maxScore(),
                t.action()
            ))
            .toList();
        return ResponseEntity.ok(thresholds);
    }

    @PostMapping("/recalculate")
    public ResponseEntity<RiskAssessResponse> recalculate(@RequestParam UUID assessmentId) {
        riskMonitoringService.recordRequest();
        long start = System.currentTimeMillis();

        var result = riskEngine.recalculate(assessmentId);

        long elapsed = System.currentTimeMillis() - start;
        riskMonitoringService.recordRecalculation(elapsed);

        riskKafkaEventPublisher.publishRiskRecalculated(
            result.id().toString(),
            result.riskLevel().name(),
            elapsed
        );

        return ResponseEntity.ok(new RiskAssessResponse(
            result.id().toString(),
            result.riskLevel() == RiskLevel.LOW || result.riskLevel() == RiskLevel.MEDIUM,
            result.riskLevel().name(),
            result.overallScore(),
            0.0,
            0.0,
            "Recalculation completed",
            "Risk re-evaluated successfully",
            System.currentTimeMillis()
        ));
    }

    @GetMapping("/trust")
    public ResponseEntity<TrustDto> getTrust(@RequestParam UUID assessmentId) {
        var trust = trustScoreRepository.findByAssessmentId(assessmentId);
        if (trust == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Double> factorScores = new HashMap<>();
        Map<String, String> factorReasons = new HashMap<>();
        trust.factorScores().forEach((k, v) -> factorScores.put(k.name(), v));
        trust.factorReasons().forEach((k, v) -> factorReasons.put(k.name(), v));

        return ResponseEntity.ok(new TrustDto(
            trust.assessmentId().toString(),
            trust.overallTrustScore(),
            factorScores,
            factorReasons,
            trust.calculatedAt().toString()
        ));
    }

    @GetMapping("/trust/history")
    public ResponseEntity<TrustHistoryDto> getTrustHistory() {
        var entries = trustScoreRepository.findAll().stream()
            .map(t -> {
                Map<String, Double> factorScores = new HashMap<>();
                Map<String, String> factorReasons = new HashMap<>();
                t.factorScores().forEach((k, v) -> factorScores.put(k.name(), v));
                t.factorReasons().forEach((k, v) -> factorReasons.put(k.name(), v));
                return new TrustDto(
                    t.assessmentId().toString(),
                    t.overallTrustScore(),
                    factorScores,
                    factorReasons,
                    t.calculatedAt().toString()
                );
            })
            .toList();
        return ResponseEntity.ok(new TrustHistoryDto(entries, entries.size()));
    }

    @GetMapping("/confidence")
    public ResponseEntity<ConfidenceDto> getConfidence(@RequestParam UUID assessmentId) {
        var confidence = confidenceScoreRepository.findByAssessmentId(assessmentId);
        if (confidence == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Double> factorScores = new HashMap<>();
        confidence.factorScores().forEach((k, v) -> factorScores.put(k.name(), v));

        return ResponseEntity.ok(new ConfidenceDto(
            confidence.assessmentId().toString(),
            confidence.overallConfidence(),
            factorScores,
            confidence.explanation(),
            confidence.calculatedAt().toString()
        ));
    }

    @GetMapping("/confidence/history")
    public ResponseEntity<ConfidenceHistoryDto> getConfidenceHistory() {
        var entries = confidenceScoreRepository.findAll().stream()
            .map(c -> {
                Map<String, Double> factorScores = new HashMap<>();
                c.factorScores().forEach((k, v) -> factorScores.put(k.name(), v));
                return new ConfidenceDto(
                    c.assessmentId().toString(),
                    c.overallConfidence(),
                    factorScores,
                    c.explanation(),
                    c.calculatedAt().toString()
                );
            })
            .toList();
        return ResponseEntity.ok(new ConfidenceHistoryDto(entries, entries.size()));
    }

    @GetMapping("/health")
    public ResponseEntity<HealthDto> health() {
        var health = riskMonitoringService.checkHealth();
        return ResponseEntity.ok(new HealthDto(
            (String) health.getOrDefault("status", "UP"),
            (String) health.getOrDefault("service", "risk"),
            (long) health.getOrDefault("timestamp", System.currentTimeMillis()),
            (Map<String, Object>) health.getOrDefault("details", Map.of())
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorDto.of(500, "Internal Server Error", ex.getMessage()));
    }
}
