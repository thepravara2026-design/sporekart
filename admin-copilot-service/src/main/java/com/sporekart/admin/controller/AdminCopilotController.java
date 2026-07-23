package com.sporekart.admin.controller;

import com.sporekart.admin.dto.*;
import com.sporekart.admin.service.AdminCopilotOrchestrator;
import com.sporekart.copilot.domain.CopilotResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/copilot/admin")
public class AdminCopilotController {

    private static final Logger log = LoggerFactory.getLogger(AdminCopilotController.class);

    private final AdminCopilotOrchestrator orchestrator;

    public AdminCopilotController(AdminCopilotOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/chat")
    public CompletableFuture<ResponseEntity<ChatResponse>> chat(@Valid @RequestBody ChatRequest request) {
        log.info("Chat request received: sessionId={}, messageLength={}", request.sessionId(), request.message().length());
        return orchestrator.processMessage(request)
            .thenApply(ResponseEntity::ok)
            .exceptionally(ex -> {
                log.error("Chat processing failed", ex);
                return ResponseEntity.internalServerError().build();
            });
    }

    @PostMapping("/stream")
    public SseEmitter stream(@Valid @RequestBody ChatRequest request) {
        log.info("Stream request received: sessionId={}", request.sessionId());
        SseEmitter emitter = new SseEmitter(300_000L);

        orchestrator.processStreamingMessage(request)
            .thenAccept(response -> {
                try {
                    emitter.send(SseEmitter.event()
                        .name("message")
                        .data(response.message()));
                    if (response.suggestions() != null && !response.suggestions().isEmpty()) {
                        emitter.send(SseEmitter.event()
                            .name("suggestions")
                            .data(response.suggestions()));
                    }
                    emitter.send(SseEmitter.event()
                        .name("complete")
                        .data(Map.of("streaming", false)));
                    emitter.complete();
                } catch (Exception e) {
                    emitter.completeWithError(e);
                }
            })
            .exceptionally(ex -> {
                emitter.completeWithError(ex);
                return null;
            });

        return emitter;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> dashboard() {
        log.info("Dashboard requested");
        return ResponseEntity.ok(orchestrator.getDashboard());
    }

    @PostMapping("/report")
    public ResponseEntity<ReportResponse> generateReport(@Valid @RequestBody ReportRequest request) {
        log.info("Report generation requested: title={}", request.title());
        return ResponseEntity.ok(orchestrator.generateReport(request));
    }

    @GetMapping("/insights")
    public ResponseEntity<InsightResponse> insights() {
        log.info("Insights requested");
        return ResponseEntity.ok(orchestrator.getInsights());
    }

    @PostMapping("/forecast")
    public ResponseEntity<ForecastResponse> forecast(@Valid @RequestBody ForecastRequest request) {
        log.info("Forecast requested: metric={}, horizon={}", request.metric(), request.horizon());
        return ResponseEntity.ok(orchestrator.getForecast(request));
    }

    @GetMapping("/alerts")
    public ResponseEntity<AlertResponse> alerts() {
        log.info("Alerts requested");
        return ResponseEntity.ok(orchestrator.getAlerts());
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "admin-copilot-service",
            "version", "0.1.0"
        ));
    }
}
