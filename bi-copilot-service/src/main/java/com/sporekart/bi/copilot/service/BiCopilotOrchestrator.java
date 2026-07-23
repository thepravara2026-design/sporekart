package com.sporekart.bi.copilot.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sporekart.bi.copilot.domain.AnomalyAlert;
import com.sporekart.bi.copilot.domain.BusinessInsight;
import com.sporekart.bi.copilot.domain.CustomerSegment;
import com.sporekart.bi.copilot.domain.ForecastResult;
import com.sporekart.bi.copilot.domain.ReportDefinition;
import com.sporekart.bi.copilot.domain.RevenueMetrics;
import com.sporekart.bi.copilot.domain.CustomerAnalytics;
import com.sporekart.bi.copilot.domain.TrainingAnalytics;
import com.sporekart.bi.copilot.domain.CultivationAnalytics;
import com.sporekart.bi.copilot.domain.CrossCopilotMetric;
import com.sporekart.bi.copilot.domain.TrendDataPoint;
import com.sporekart.bi.copilot.dto.AnomaliesResponse;
import com.sporekart.bi.copilot.dto.BiQueryRequest;
import com.sporekart.bi.copilot.dto.BiQueryResponse;
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
import com.sporekart.bi.copilot.engine.RevenueAnalyticsEngine;
import com.sporekart.bi.copilot.engine.CustomerAnalyticsEngine;
import com.sporekart.bi.copilot.engine.TrainingAnalyticsEngine;
import com.sporekart.bi.copilot.engine.CultivationAnalyticsEngine;
import com.sporekart.bi.copilot.engine.CrossCopilotIntelligenceEngine;
import com.sporekart.bi.copilot.engine.TrendDetectionEngine;
import com.sporekart.bi.copilot.engine.AnomalyDetectionEngine;
import com.sporekart.bi.copilot.engine.BusinessInsightsEngine;
import com.sporekart.bi.copilot.engine.ForecastingEngine;
import com.sporekart.bi.copilot.engine.CustomerSegmentationEngine;
import com.sporekart.bi.copilot.engine.ReportingEngine;
import com.sporekart.bi.copilot.engine.DashboardEngine;
import com.sporekart.bi.copilot.infrastructure.monitoring.BiMetricsService;

@Service
public class BiCopilotOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(BiCopilotOrchestrator.class);

    private final Map<String, CopilotSession> sessions = new ConcurrentHashMap<>();

    private final RevenueAnalyticsEngine revenueAnalyticsEngine;
    private final CustomerAnalyticsEngine customerAnalyticsEngine;
    private final TrainingAnalyticsEngine trainingAnalyticsEngine;
    private final CultivationAnalyticsEngine cultivationAnalyticsEngine;
    private final CrossCopilotIntelligenceEngine crossCopilotIntelligenceEngine;
    private final TrendDetectionEngine trendDetectionEngine;
    private final AnomalyDetectionEngine anomalyDetectionEngine;
    private final BusinessInsightsEngine businessInsightsEngine;
    private final ForecastingEngine forecastingEngine;
    private final CustomerSegmentationEngine customerSegmentationEngine;
    private final ReportingEngine reportingEngine;
    private final DashboardEngine dashboardEngine;
    private final BiMetricsService biMetricsService;

    public BiCopilotOrchestrator(
            RevenueAnalyticsEngine revenueAnalyticsEngine,
            CustomerAnalyticsEngine customerAnalyticsEngine,
            TrainingAnalyticsEngine trainingAnalyticsEngine,
            CultivationAnalyticsEngine cultivationAnalyticsEngine,
            CrossCopilotIntelligenceEngine crossCopilotIntelligenceEngine,
            TrendDetectionEngine trendDetectionEngine,
            AnomalyDetectionEngine anomalyDetectionEngine,
            BusinessInsightsEngine businessInsightsEngine,
            ForecastingEngine forecastingEngine,
            CustomerSegmentationEngine customerSegmentationEngine,
            ReportingEngine reportingEngine,
            DashboardEngine dashboardEngine,
            BiMetricsService biMetricsService) {
        this.revenueAnalyticsEngine = revenueAnalyticsEngine;
        this.customerAnalyticsEngine = customerAnalyticsEngine;
        this.trainingAnalyticsEngine = trainingAnalyticsEngine;
        this.cultivationAnalyticsEngine = cultivationAnalyticsEngine;
        this.crossCopilotIntelligenceEngine = crossCopilotIntelligenceEngine;
        this.trendDetectionEngine = trendDetectionEngine;
        this.anomalyDetectionEngine = anomalyDetectionEngine;
        this.businessInsightsEngine = businessInsightsEngine;
        this.forecastingEngine = forecastingEngine;
        this.customerSegmentationEngine = customerSegmentationEngine;
        this.reportingEngine = reportingEngine;
        this.dashboardEngine = dashboardEngine;
        this.biMetricsService = biMetricsService;
        log.info("BiCopilotOrchestrator initialized");
    }

    public ChatResponse processMessage(String sessionId, String message, Map<String, Object> userContext, Map<String, Object> pageContext) {
        log.debug("Processing message for session {}: {}", sessionId, message);
        CopilotSession session = sessions.computeIfAbsent(sessionId, k -> new CopilotSession(sessionId));
        session.addMessage(message);
        biMetricsService.incrementMessagesProcessed();

        String intent = resolveIntent(message, pageContext);
        Map<String, Object> responseContext = new LinkedHashMap<>();
        responseContext.put("intent", intent);
        responseContext.put("pageContext", pageContext);

        return new ChatResponse(
            sessionId,
            "Processed: " + intent + " | " + message,
            List.of(),
            responseContext,
            false
        );
    }

    public ChatResponse processChat(String sessionId, String message) {
        return processMessage(sessionId, message, Map.of(), Map.of());
    }

    public String createSession() {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, new CopilotSession(sessionId));
        log.info("Created BI copilot session: {}", sessionId);
        biMetricsService.incrementSessionsCreated();
        return sessionId;
    }

    public void endSession(String sessionId) {
        sessions.remove(sessionId);
        log.info("Ended BI copilot session: {}", sessionId);
        biMetricsService.incrementSessionsEnded();
    }

    public DashboardResponse getDashboard(String dashboardId) {
        return new DashboardResponse(
            dashboardId,
            "Dashboard " + dashboardId,
            List.of(),
            Map.of(),
            List.of(),
            OffsetDateTime.now()
        );
    }

    public DashboardResponse getDefaultDashboard() {
        return getDashboard("default");
    }

    public BiQueryResponse queryData(BiQueryRequest request) {
        List<Map<String, Object>> results = new ArrayList<>();
        results.add(Map.of(
            "source", request.dataSource(),
            "query", request.query(),
            "result", "Simulated data for " + request.dataSource()
        ));
        return new BiQueryResponse(results, results.size(), request.page(), request.size(), 45L, "Simulated query");
    }

    public ReportResponse generateReport(ReportRequest request) {
        ReportDefinition definition = new ReportDefinition(
            UUID.randomUUID().toString(),
            "Report",
            request.reportType(),
            request.format(),
            request.schedule(),
            request.metrics(),
            request.dimensions(),
            request.filters(),
            request.recipients(),
            "completed",
            OffsetDateTime.now()
        );
        Map<String, Object> report = reportingEngine.generateReport(definition);
        return new ReportResponse(
            definition.reportId(),
            definition.name(),
            "completed",
            definition.format(),
            "/reports/" + definition.reportId(),
            report,
            OffsetDateTime.now()
        );
    }

    public InsightsResponse getInsights(String category, String period) {
        List<BusinessInsight> insights = businessInsightsEngine.generateInsights(category, period);
        return new InsightsResponse(insights, insights.size(), category, period);
    }

    public TrendsResponse getTrends(String metric, String period) {
        List<Double> sampleData = List.of(100.0, 110.0, 120.0, 130.0, 125.0, 140.0, 150.0, 160.0, 155.0, 170.0, 180.0, 190.0);
        List<TrendDataPoint> trends = trendDetectionEngine.detectTrends(metric, sampleData);
        return new TrendsResponse(trends, metric, period, trends.size(), Map.of("average", 140.0));
    }

    public AnomaliesResponse getAnomalies(String period) {
        List<AnomalyAlert> anomalies = anomalyDetectionEngine.detectAnomaliesAcrossMetrics();
        long critical = anomalies.stream().filter(a -> "critical".equals(a.severity())).count();
        long unresolved = anomalies.stream().filter(a -> !a.autoResolved()).count();
        return new AnomaliesResponse(anomalies, anomalies.size(), (int) critical, (int) unresolved, period);
    }

    public ForecastResponse forecast(ForecastRequest request) {
        List<Double> historicalData = List.of(400000.0, 420000.0, 450000.0, 480000.0, 510000.0, 550000.0);
        ForecastResult result = forecastingEngine.forecast(request.metric(), request.method(), request.horizon(), historicalData);
        List<ForecastResponse.ForecastPoint> points = result.points().stream()
            .map(p -> new ForecastResponse.ForecastPoint(p.period(), p.predictedValue(), p.lowerBound(), p.upperBound()))
            .toList();
        return new ForecastResponse(result.forecastId(), result.metric(), result.method(), points, result.confidenceInterval(), result.recommendations());
    }

    public CrossCopilotMetricsResponse getCrossCopilotMetrics() {
        List<CrossCopilotMetric> metrics = crossCopilotIntelligenceEngine.getCrossCopilotMetrics();
        Map<String, Object> comparison = crossCopilotIntelligenceEngine.getCopilotPerformanceComparison();
        return new CrossCopilotMetricsResponse(metrics, OffsetDateTime.now(), comparison);
    }

    public CustomerSegmentsResponse getCustomerSegments() {
        List<CustomerSegment> segments = customerSegmentationEngine.segmentCustomers();
        int totalCustomers = segments.stream().mapToInt(CustomerSegment::customerCount).sum();
        double totalRevenue = segments.stream().mapToDouble(CustomerSegment::totalRevenue).sum();
        return new CustomerSegmentsResponse(segments, totalCustomers, totalRevenue, OffsetDateTime.now());
    }

    private String resolveIntent(String message, Map<String, Object> pageContext) {
        String lower = message.toLowerCase();
        if (lower.contains("revenue") || lower.contains("sales")) return "revenue_query";
        if (lower.contains("customer") || lower.contains("user")) return "customer_query";
        if (lower.contains("train") || lower.contains("course")) return "training_query";
        if (lower.contains("yield") || lower.contains("cultivat") || lower.contains("grow")) return "cultivation_query";
        if (lower.contains("forecast") || lower.contains("predict")) return "forecast_request";
        if (lower.contains("trend")) return "trend_request";
        if (lower.contains("anomal") || lower.contains("alert")) return "anomaly_request";
        if (lower.contains("insight")) return "insight_request";
        if (lower.contains("report")) return "report_request";
        if (lower.contains("dashboard")) return "dashboard_request";
        if (lower.contains("segment")) return "segment_request";
        return "general_query";
    }

    private record CopilotSession(
        String sessionId,
        List<String> messages,
        OffsetDateTime createdAt,
        OffsetDateTime lastActivityAt
    ) {
        public CopilotSession(String sessionId) {
            this(sessionId, new ArrayList<>(), OffsetDateTime.now(), OffsetDateTime.now());
        }

        public CopilotSession addMessage(String message) {
            messages.add(message);
            return new CopilotSession(sessionId, messages, createdAt, OffsetDateTime.now());
        }
    }
}
