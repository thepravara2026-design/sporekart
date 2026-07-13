package com.sporekart.ai.decision.interfaces.rest;
import com.sporekart.ai.decision.api.*;
import com.sporekart.ai.decision.domain.*;
import com.sporekart.ai.decision.interfaces.rest.dto.*;
import com.sporekart.ai.decision.infrastructure.kafka.*;
import com.sporekart.ai.decision.infrastructure.monitoring.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.OffsetDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/decisions")
@RequiredArgsConstructor
public class DecisionController {
    private final DecisionEngine engine;
    private final DecisionResolver resolver;
    private final DecisionExplanationService explanationService;
    private final DecisionMetricsService metricsService;
    private final DecisionKafkaEventPublisher eventPublisher;
    private final DecisionMonitoringService monitoringService;

    @PostMapping("/evaluate")
    public ResponseEntity<DecisionResponseDto> evaluate(@RequestBody DecisionRequestDto request) {
        long start = System.currentTimeMillis();
        DecisionRequest domainReq = new DecisionRequest(UUID.randomUUID(), request.module(), request.action(),
            request.payload() != null ? request.payload() : new HashMap<>(),
            request.context() != null ? request.context() : new HashMap<>(),
            request.userId(), request.roles() != null ? request.roles() : List.of(),
            List.of(), List.of(), new HashMap<>(), OffsetDateTime.now());
        DecisionResult result = engine.evaluate(domainReq);
        long elapsed = System.currentTimeMillis() - start;
        monitoringService.recordDecision(elapsed);
        eventPublisher.publishEvaluated(domainReq.id().toString(), result.action().name(), result.status().name(), elapsed);
        return ResponseEntity.ok(new DecisionResponseDto(result.id().toString(), result.requestId().toString(),
            result.action().name(), result.status().name(), result.confidence().name(), result.summary(),
            result.reasons().stream().map(r -> new ReasonDto(r.code(), r.message(), r.category(), r.confidence().name(), r.details())).toList(),
            elapsed, result.requiresApproval()));
    }

    @GetMapping
    public ResponseEntity<List<DecisionResponseDto>> listDecisions() {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DecisionResponseDto> getDecision(@PathVariable UUID id) {
        return resolver.findById(id)
            .map(r -> ResponseEntity.ok(new DecisionResponseDto(r.id().toString(), r.requestId().toString(),
                r.action().name(), r.status().name(), r.confidence().name(), r.summary(), List.of(), r.processingTimeMs(), r.requiresApproval())))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/history")
    public ResponseEntity<HistoryDto> getHistory() {
        return ResponseEntity.ok(new HistoryDto(List.of(), 0));
    }

    @GetMapping("/explanations")
    public ResponseEntity<List<ExplanationDto>> getExplanations(@RequestParam(required = false) String decisionId) {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsDto> getStatistics() {
        DecisionStatistics stats = metricsService.getStatistics();
        return ResponseEntity.ok(new StatisticsDto(stats.totalDecisions(), stats.allowedCount(), stats.deniedCount(),
            stats.escalatedCount(), stats.approvalCount(), metricsService.getDetailedMetrics()));
    }

    @GetMapping("/health")
    public ResponseEntity<HealthDto> health() {
        return ResponseEntity.ok(new HealthDto("UP", "decision-engine", System.currentTimeMillis(),
            Map.of("metrics", metricsService.getDetailedMetrics())));
    }

    @PostMapping("/replay")
    public ResponseEntity<ReplayDto> replay(@RequestBody Map<String, String> request) {
        String requestId = request.getOrDefault("requestId", UUID.randomUUID().toString());
        eventPublisher.publishReplayStarted(requestId);
        engine.replay(UUID.fromString(requestId));
        eventPublisher.publishReplayCompleted(requestId);
        return ResponseEntity.ok(new ReplayDto(true, "Replay completed", requestId, System.currentTimeMillis()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDto.of(500, "Internal Server Error", ex.getMessage()));
    }
}
