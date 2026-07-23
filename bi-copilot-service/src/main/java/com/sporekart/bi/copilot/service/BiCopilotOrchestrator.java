package com.sporekart.bi.copilot.service;

import com.sporekart.bi.copilot.domain.CompanyHealthScore;
import com.sporekart.bi.copilot.domain.CustomerAnalytics;
import com.sporekart.bi.copilot.domain.InventoryAnalytics;
import com.sporekart.bi.copilot.domain.ProductAnalytics;
import com.sporekart.bi.copilot.domain.RevenueAnalytics;
import com.sporekart.bi.copilot.domain.TrainingAnalytics;
import com.sporekart.bi.copilot.dto.ChatResponse;
import com.sporekart.bi.copilot.dto.DashboardResponse;
import com.sporekart.bi.copilot.dto.DecisionSupportResponse;
import com.sporekart.bi.copilot.dto.ForecastRequest;
import com.sporekart.bi.copilot.dto.ForecastResponse;
import com.sporekart.bi.copilot.dto.HealthScoreResponse;
import com.sporekart.bi.copilot.dto.InsightsResponse;
import com.sporekart.bi.copilot.dto.KpiResponse;
import com.sporekart.bi.copilot.dto.NaturalLanguageQueryRequest;
import com.sporekart.bi.copilot.dto.NaturalLanguageQueryResponse;
import com.sporekart.bi.copilot.dto.ReportRequest;
import com.sporekart.bi.copilot.dto.ReportResponse;
import com.sporekart.bi.copilot.dto.RiskResponse;
import com.sporekart.bi.copilot.engine.BusinessInsightsEngine;
import com.sporekart.bi.copilot.engine.CustomerAnalyticsEngine;
import com.sporekart.bi.copilot.engine.DecisionSupportEngine;
import com.sporekart.bi.copilot.engine.ExecutiveDashboardEngine;
import com.sporekart.bi.copilot.engine.ForecastingEngine;
import com.sporekart.bi.copilot.engine.InventoryAnalyticsEngine;
import com.sporekart.bi.copilot.engine.NaturalLanguageAnalyticsEngine;
import com.sporekart.bi.copilot.engine.ProductAnalyticsEngine;
import com.sporekart.bi.copilot.engine.RevenueAnalyticsEngine;
import com.sporekart.bi.copilot.engine.RiskDetectionEngine;
import com.sporekart.bi.copilot.engine.TrainingAnalyticsEngine;
import com.sporekart.bi.copilot.engine.VisualizationEngine;
import com.sporekart.bi.copilot.infrastructure.monitoring.BiMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BiCopilotOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(BiCopilotOrchestrator.class);

    private final RevenueAnalyticsEngine revenueAnalyticsEngine;
    private final CustomerAnalyticsEngine customerAnalyticsEngine;
    private final ProductAnalyticsEngine productAnalyticsEngine;
    private final InventoryAnalyticsEngine inventoryAnalyticsEngine;
    private final TrainingAnalyticsEngine trainingAnalyticsEngine;
    private final ExecutiveDashboardEngine executiveDashboardEngine;
    private final ForecastingEngine forecastingEngine;
    private final DecisionSupportEngine decisionSupportEngine;
    private final RiskDetectionEngine riskDetectionEngine;
    private final NaturalLanguageAnalyticsEngine naturalLanguageAnalyticsEngine;
    private final VisualizationEngine visualizationEngine;
    private final BusinessInsightsEngine businessInsightsEngine;
    private final BiMetricsService biMetricsService;

    public BiCopilotOrchestrator(RevenueAnalyticsEngine revenueAnalyticsEngine,
                                 CustomerAnalyticsEngine customerAnalyticsEngine,
                                 ProductAnalyticsEngine productAnalyticsEngine,
                                 InventoryAnalyticsEngine inventoryAnalyticsEngine,
                                 TrainingAnalyticsEngine trainingAnalyticsEngine,
                                 ExecutiveDashboardEngine executiveDashboardEngine,
                                 ForecastingEngine forecastingEngine,
                                 DecisionSupportEngine decisionSupportEngine,
                                 RiskDetectionEngine riskDetectionEngine,
                                 NaturalLanguageAnalyticsEngine naturalLanguageAnalyticsEngine,
                                 VisualizationEngine visualizationEngine,
                                 BusinessInsightsEngine businessInsightsEngine,
                                 BiMetricsService biMetricsService) {
        this.revenueAnalyticsEngine = revenueAnalyticsEngine;
        this.customerAnalyticsEngine = customerAnalyticsEngine;
        this.productAnalyticsEngine = productAnalyticsEngine;
        this.inventoryAnalyticsEngine = inventoryAnalyticsEngine;
        this.trainingAnalyticsEngine = trainingAnalyticsEngine;
        this.executiveDashboardEngine = executiveDashboardEngine;
        this.forecastingEngine = forecastingEngine;
        this.decisionSupportEngine = decisionSupportEngine;
        this.riskDetectionEngine = riskDetectionEngine;
        this.naturalLanguageAnalyticsEngine = naturalLanguageAnalyticsEngine;
        this.visualizationEngine = visualizationEngine;
        this.businessInsightsEngine = businessInsightsEngine;
        this.biMetricsService = biMetricsService;
    }

    public ChatResponse processMessage(String sessionId, String message, Map<String, String> userContext, Map<String, String> pageContext) {
        biMetricsService.recordQueryLatency(System.currentTimeMillis());
        String lower = message.toLowerCase();

        ChatResponse response;
        if (matchesAny(lower, "dashboard", "health", "overview", "snapshot")) {
            var dashboard = getExecutiveDashboard("current");
            response = new ChatResponse(sessionId, "Executive dashboard generated", List.of(), Map.of("dashboard", dashboard), false);
        } else if (matchesAny(lower, "revenue", "sales", "orders", "aov")) {
            var revenue = getRevenueAnalytics("current");
            response = new ChatResponse(sessionId, "Revenue analytics generated", List.of(), Map.of("revenue", revenue), false);
        } else if (matchesAny(lower, "customer", "retention", "churn", "segments")) {
            var customer = getCustomerAnalytics("current");
            response = new ChatResponse(sessionId, "Customer analytics generated", List.of(), Map.of("customer", customer), false);
        } else if (matchesAny(lower, "product", "top", "worst", "fast", "slow")) {
            var product = getProductAnalytics("current");
            response = new ChatResponse(sessionId, "Product analytics generated", List.of(), Map.of("product", product), false);
        } else if (matchesAny(lower, "inventory", "stock", "low", "dead")) {
            var inventory = getInventoryAnalytics("current");
            response = new ChatResponse(sessionId, "Inventory analytics generated", List.of(), Map.of("inventory", inventory), false);
        } else if (matchesAny(lower, "training", "batch", "student", "certification")) {
            var training = getTrainingAnalytics("current");
            response = new ChatResponse(sessionId, "Training analytics generated", List.of(), Map.of("training", training), false);
        } else if (matchesAny(lower, "forecast", "predict", "next", "trend")) {
            var forecast = getForecast(new ForecastRequest("revenue", "current", 12, "auto"));
            response = new ChatResponse(sessionId, "Forecast generated", List.of(), Map.of("forecast", forecast), false);
        } else if (matchesAny(lower, "recommend", "suggest", "what should")) {
            var recommendations = getRecommendations("general");
            response = new ChatResponse(sessionId, "Recommendations generated", List.of(), Map.of("recommendations", recommendations), false);
        } else if (matchesAny(lower, "risk", "anomaly", "problem", "alert")) {
            var risks = getRisks("current");
            response = new ChatResponse(sessionId, "Risk analysis generated", List.of(), Map.of("risks", risks), false);
        } else {
            response = new ChatResponse(sessionId, "I can help with business intelligence queries about revenue, customers, products, inventory, training, forecasts, risks, and recommendations.", List.of(), Map.of(), false);
        }

        biMetricsService.recordQueryLatency(System.currentTimeMillis());
        return response;
    }

    private boolean matchesAny(String text, String... keywords) {
        for (String kw : keywords) {
            if (text.contains(kw)) return true;
        }
        return false;
    }

    public DashboardResponse getExecutiveDashboard(String period) {
        return executiveDashboardEngine.getDashboard(period);
    }

    public RevenueAnalytics getRevenueAnalytics(String period) {
        return revenueAnalyticsEngine.getRevenueSummary(period);
    }

    public CustomerAnalytics getCustomerAnalytics(String period) {
        return customerAnalyticsEngine.getCustomerSummary(period);
    }

    public ProductAnalytics getProductAnalytics(String period) {
        return productAnalyticsEngine.getProductSummary(period);
    }

    public InventoryAnalytics getInventoryAnalytics(String period) {
        return inventoryAnalyticsEngine.getInventorySummary(period);
    }

    public TrainingAnalytics getTrainingAnalytics(String period) {
        return trainingAnalyticsEngine.getTrainingSummary(period);
    }

    public ReportResponse generateReport(ReportRequest request) {
        return new ReportResponse("report-" + System.currentTimeMillis(), "BI Report", request.format(), "generated", "/reports/bi/" + System.currentTimeMillis(), 0, java.time.OffsetDateTime.now());
    }

    public ForecastResponse getForecast(ForecastRequest request) {
        var forecast = forecastingEngine.forecast(request);
        var visualizations = visualizationEngine.generateVisualizations("forecast", "line");
        return new ForecastResponse(forecast, visualizations);
    }

    public KpiResponse getKpis(String period) {
        var revenue = getRevenueAnalytics(period);
        var customers = getCustomerAnalytics(period);
        var training = getTrainingAnalytics(period);

        return new KpiResponse(List.of(
            new KpiResponse.KpiEntry("kpi-001", "Gross Revenue", revenue.grossRevenue(), revenue.previousPeriodRevenue(),
                revenue.revenueGrowth(), revenue.revenueGrowth() >= 0 ? "up" : "down", "INR", "healthy"),
            new KpiResponse.KpiEntry("kpi-002", "AOV", revenue.averageOrderValue(), 0, 0, "stable", "INR", "healthy"),
            new KpiResponse.KpiEntry("kpi-003", "Retention Rate", customers.retentionRate(), 0, 0, "stable", "%", "good"),
            new KpiResponse.KpiEntry("kpi-004", "Churn Rate", customers.churnRate(), 0, 0, customers.churnRate() > 10 ? "up" : "down", "%", customers.churnRate() > 10 ? "warning" : "good"),
            new KpiResponse.KpiEntry("kpi-005", "Total Customers", customers.totalCustomers(), 0, 0, "stable", "count", "healthy"),
            new KpiResponse.KpiEntry("kpi-006", "Avg Score", training.averageScore(), 0, 0, "stable", "%", "good"),
            new KpiResponse.KpiEntry("kpi-007", "Completion Rate", training.completionRate(), 0, 0, "stable", "%", "good"),
            new KpiResponse.KpiEntry("kpi-008", "Certifications", training.certificationsIssued(), 0, 0, "stable", "count", "good")
        ), period, java.time.OffsetDateTime.now());
    }

    public InsightsResponse getInsights(String category, String period, boolean actionableOnly) {
        var insights = businessInsightsEngine.getInsights(category, period, actionableOnly);
        long critical = insights.stream().filter(i -> "critical".equalsIgnoreCase(i.severity())).count();
        long important = insights.stream().filter(i -> "important".equalsIgnoreCase(i.severity())).count();
        long info = insights.stream().filter(i -> "info".equalsIgnoreCase(i.severity())).count();
        return new InsightsResponse(insights, insights.size(), (int) critical, (int) important, (int) info);
    }

    public DecisionSupportResponse getRecommendations(String focus) {
        var recommendations = decisionSupportEngine.getRecommendations(focus);
        return new DecisionSupportResponse(recommendations, focus, "current");
    }

    public RiskResponse getRisks(String period) {
        var risks = riskDetectionEngine.getRisks(period);
        long critical = risks.stream().filter(r -> "critical".equalsIgnoreCase(r.severity())).count();
        long high = risks.stream().filter(r -> "high".equalsIgnoreCase(r.severity())).count();
        long open = risks.stream().filter(r -> "open".equalsIgnoreCase(r.status())).count();
        return new RiskResponse(risks, risks.size(), (int) critical, (int) high, (int) open, period);
    }

    public NaturalLanguageQueryResponse answerQuery(NaturalLanguageQueryRequest request) {
        return naturalLanguageAnalyticsEngine.answerQuery(request);
    }

    public HealthScoreResponse getHealthScore() {
        var healthScore = executiveDashboardEngine.getHealthScore();
        var visualizations = visualizationEngine.generateVisualizations("healthScore", "radar");
        return new HealthScoreResponse(healthScore, visualizations);
    }
}
