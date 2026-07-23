package com.sporekart.bi.copilot.controller;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sporekart.bi.copilot.dto.AnomaliesResponse;
import com.sporekart.bi.copilot.dto.BiQueryRequest;
import com.sporekart.bi.copilot.dto.BiQueryResponse;
import com.sporekart.bi.copilot.dto.ChatRequest;
import com.sporekart.bi.copilot.dto.ChatResponse;
import com.sporekart.bi.copilot.dto.CrossCopilotMetricsResponse;
import com.sporekart.bi.copilot.dto.CustomerSegmentsResponse;
import com.sporekart.bi.copilot.dto.DashboardResponse;
import com.sporekart.bi.copilot.dto.ForecastRequest;
import com.sporekart.bi.copilot.dto.ForecastResponse;
import com.sporekart.bi.copilot.dto.InsightsResponse;
import com.sporekart.bi.copilot.dto.ReportRequest;
import com.sporekart.bi.copilot.dto.ReportResponse;
import com.sporekart.bi.copilot.dto.TrendsResponse;
import com.sporekart.bi.copilot.service.BiCopilotOrchestrator;

@RestController
@RequestMapping("/api/v1/copilot/bi")
public class BiCopilotController {

    private final BiCopilotOrchestrator orchestrator;

    public BiCopilotController(BiCopilotOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String sessionId = request.sessionId() != null ? request.sessionId() : orchestrator.createSession();
        return orchestrator.processMessage(sessionId, request.message(), Map.of(), buildPageContext(request));
    }

    @PostMapping("/stream")
    public SseEmitter stream(@RequestBody ChatRequest request) {
        SseEmitter emitter = new SseEmitter(300000L);
        String sessionId = request.sessionId() != null ? request.sessionId() : orchestrator.createSession();
        ChatResponse response = orchestrator.processMessage(sessionId, request.message(), Map.of(), buildPageContext(request));
        try {
            emitter.send(SseEmitter.event().name("message").data(response));
            emitter.complete();
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

    @PostMapping("/query")
    public BiQueryResponse query(@RequestBody BiQueryRequest request) {
        return orchestrator.queryData(request);
    }

    @GetMapping("/dashboard")
    public DashboardResponse getDashboard(@RequestParam(required = false) String dashboardId) {
        if (dashboardId != null) {
            return orchestrator.getDashboard(dashboardId);
        }
        return orchestrator.getDefaultDashboard();
    }

    @GetMapping("/dashboard/{dashboardId}")
    public DashboardResponse getDashboardById(@PathVariable String dashboardId) {
        return orchestrator.getDashboard(dashboardId);
    }

    @PostMapping("/report")
    public ReportResponse generateReport(@RequestBody ReportRequest request) {
        return orchestrator.generateReport(request);
    }

    @GetMapping("/insights")
    public InsightsResponse getInsights(
            @RequestParam(defaultValue = "revenue") String category,
            @RequestParam(defaultValue = "current") String period) {
        return orchestrator.getInsights(category, period);
    }

    @GetMapping("/trends")
    public TrendsResponse getTrends(
            @RequestParam(defaultValue = "revenue") String metric,
            @RequestParam(defaultValue = "12m") String period) {
        return orchestrator.getTrends(metric, period);
    }

    @GetMapping("/anomalies")
    public AnomaliesResponse getAnomalies(@RequestParam(defaultValue = "current") String period) {
        return orchestrator.getAnomalies(period);
    }

    @PostMapping("/forecast")
    public ForecastResponse forecast(@RequestBody ForecastRequest request) {
        return orchestrator.forecast(request);
    }

    @GetMapping("/cross-copilot")
    public CrossCopilotMetricsResponse getCrossCopilotMetrics() {
        return orchestrator.getCrossCopilotMetrics();
    }

    @GetMapping("/segments")
    public CustomerSegmentsResponse getCustomerSegments() {
        return orchestrator.getCustomerSegments();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "bi-copilot-service",
            "timestamp", OffsetDateTime.now().toString()
        ));
    }

    private Map<String, Object> buildPageContext(ChatRequest request) {
        return Map.of(
            "pageUrl", request.pageUrl() != null ? request.pageUrl() : "",
            "pageTitle", request.pageTitle() != null ? request.pageTitle() : "",
            "section", request.section() != null ? request.section() : "",
            "entityType", request.entityType() != null ? request.entityType() : "",
            "entityId", request.entityId() != null ? request.entityId() : ""
        );
    }
}
