package com.sporekart.bi.copilot.controller;

import com.sporekart.bi.copilot.dto.ChatRequest;
import com.sporekart.bi.copilot.dto.ChatResponse;
import com.sporekart.bi.copilot.dto.DashboardResponse;
import com.sporekart.bi.copilot.dto.DecisionSupportResponse;
import com.sporekart.bi.copilot.dto.ForecastRequest;
import com.sporekart.bi.copilot.dto.ForecastResponse;
import com.sporekart.bi.copilot.dto.HealthScoreResponse;
import com.sporekart.bi.copilot.dto.InsightsRequest;
import com.sporekart.bi.copilot.dto.InsightsResponse;
import com.sporekart.bi.copilot.dto.KpiResponse;
import com.sporekart.bi.copilot.dto.NaturalLanguageQueryRequest;
import com.sporekart.bi.copilot.dto.NaturalLanguageQueryResponse;
import com.sporekart.bi.copilot.dto.ReportRequest;
import com.sporekart.bi.copilot.dto.ReportResponse;
import com.sporekart.bi.copilot.dto.RiskResponse;
import com.sporekart.bi.copilot.service.BiCopilotOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/v1/copilot/bi")
public class BiCopilotController {

    private static final Logger log = LoggerFactory.getLogger(BiCopilotController.class);

    private final BiCopilotOrchestrator orchestrator;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public BiCopilotController(BiCopilotOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        log.info("BI copilot chat request: sessionId={}, message={}", request.sessionId(), request.message());
        var response = orchestrator.processMessage(
            request.sessionId(), request.message(),
            Map.of("pageUrl", request.pageUrl(), "pageTitle", request.pageTitle()),
            Map.of("section", request.section()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stream")
    public SseEmitter stream(@RequestBody ChatRequest request) {
        log.info("BI copilot stream request: sessionId={}, message={}", request.sessionId(), request.message());
        SseEmitter emitter = new SseEmitter(300_000L);

        executor.execute(() -> {
            try {
                var response = orchestrator.processMessage(
                    request.sessionId(), request.message(),
                    Map.of("pageUrl", request.pageUrl(), "pageTitle", request.pageTitle()),
                    Map.of("section", request.section()));

                emitter.send(SseEmitter.event()
                    .name("message")
                    .data(response, MediaType.APPLICATION_JSON));
                emitter.send(SseEmitter.event()
                    .name("complete")
                    .data("{\"status\":\"done\"}", MediaType.APPLICATION_JSON));
                emitter.complete();
            } catch (IOException e) {
                log.error("SSE stream error for session {}", request.sessionId(), e);
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard(@RequestParam(defaultValue = "current") String period) {
        log.info("Get BI dashboard for period: {}", period);
        return ResponseEntity.ok(orchestrator.getExecutiveDashboard(period));
    }

    @PostMapping("/report")
    public ResponseEntity<ReportResponse> generateReport(@RequestBody ReportRequest request) {
        log.info("Generate BI report: type={}, format={}", request.reportType(), request.format());
        return ResponseEntity.ok(orchestrator.generateReport(request));
    }

    @PostMapping("/forecast")
    public ResponseEntity<ForecastResponse> getForecast(@RequestBody ForecastRequest request) {
        log.info("Get BI forecast: metric={}, horizon={}", request.metric(), request.horizon());
        return ResponseEntity.ok(orchestrator.getForecast(request));
    }

    @GetMapping("/kpis")
    public ResponseEntity<KpiResponse> getKpis(@RequestParam(defaultValue = "current") String period) {
        log.info("Get BI KPIs for period: {}", period);
        return ResponseEntity.ok(orchestrator.getKpis(period));
    }

    @PostMapping("/insights")
    public ResponseEntity<InsightsResponse> getInsights(@RequestBody InsightsRequest request) {
        log.info("Get BI insights: category={}, period={}", request.category(), request.period());
        return ResponseEntity.ok(orchestrator.getInsights(request.category(), request.period(), request.actionableOnly()));
    }

    @PostMapping("/recommendations")
    public ResponseEntity<DecisionSupportResponse> getRecommendations(@RequestBody Map<String, String> body) {
        String focus = body.getOrDefault("focus", "general");
        log.info("Get BI recommendations for focus: {}", focus);
        return ResponseEntity.ok(orchestrator.getRecommendations(focus));
    }

    @GetMapping("/risks")
    public ResponseEntity<RiskResponse> getRisks(@RequestParam(defaultValue = "current") String period) {
        log.info("Get BI risks for period: {}", period);
        return ResponseEntity.ok(orchestrator.getRisks(period));
    }

    @PostMapping("/query")
    public ResponseEntity<NaturalLanguageQueryResponse> query(@RequestBody NaturalLanguageQueryRequest request) {
        log.info("BI natural language query: {}", request.query());
        return ResponseEntity.ok(orchestrator.answerQuery(request));
    }

    @GetMapping("/health-score")
    public ResponseEntity<HealthScoreResponse> getHealthScore() {
        log.info("Get BI company health score");
        return ResponseEntity.ok(orchestrator.getHealthScore());
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "service", "bi-copilot",
            "status", "UP",
            "timestamp", java.time.OffsetDateTime.now()));
    }
}
