package com.sporekart.admin.service;

import com.sporekart.admin.domain.AdminDashboard;
import com.sporekart.admin.domain.BusinessInsight;
import com.sporekart.admin.domain.ForecastResult;
import com.sporekart.admin.domain.OperationalAlert;
import com.sporekart.admin.dto.*;
import com.sporekart.copilot.context.PageContext;
import com.sporekart.copilot.context.UserContext;
import com.sporekart.copilot.core.CopilotEngine;
import com.sporekart.copilot.domain.CopilotResponse;
import com.sporekart.copilot.domain.CopilotType;
import com.sporekart.copilot.domain.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class AdminCopilotOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(AdminCopilotOrchestrator.class);

    private final CopilotEngine engine;
    private final AdminContextService contextService;

    public AdminCopilotOrchestrator(AdminCopilotRegistrationService registrationService,
                                    AdminContextService contextService) {
        this.engine = registrationService.getEngine();
        this.contextService = contextService;
    }

    public CompletableFuture<ChatResponse> processMessage(ChatRequest request) {
        SessionId sessionId = resolveSessionId(request.sessionId());
        UserContext userContext = contextService.getUserContext(sessionId);

        PageContext pageContext = PageContext.builder()
            .pageUrl(request.pageUrl())
            .pageTitle(request.pageTitle())
            .section(request.section())
            .entityType(request.entityType())
            .entityId(request.entityId())
            .build();

        contextService.trackQuery(sessionId, request.message());

        return engine.processMessage(sessionId, request.message(), userContext, pageContext)
            .thenApply(response -> new ChatResponse(
                sessionId.toString(),
                response.message(),
                response.suggestions(),
                response.context(),
                response.streaming()
            ))
            .exceptionally(ex -> {
                log.error("Error processing message for session {}: {}", sessionId, ex.getMessage(), ex);
                return new ChatResponse(
                    sessionId.toString(),
                    "I encountered an error processing your request. Please try again.",
                    List.of(),
                    Map.of(),
                    false
                );
            });
    }

    public DashboardResponse getDashboard() {
        SessionId sessionId = new SessionId();
        UserContext userContext = contextService.buildUserContext("system", "Admin", "admin@sporekart.com");
        contextService.initSession(sessionId, userContext);

        AdminDashboard dashboard = new AdminDashboard(
            BigDecimal.valueOf(1250000), 450, 8920, 3400, 1250, 23, 18,
            BigDecimal.valueOf(48500), BigDecimal.valueOf(12.5),
            LocalDateTime.now().minusDays(30), LocalDateTime.now()
        );

        BusinessInsight insight = new BusinessInsight(
            "Strong revenue growth driven by seasonal demand in the produce category. Customer acquisition costs remain stable.",
            Map.of("revenueGrowth", 12.5, "orderGrowth", 8.3, "customerGrowth", 5.1),
            List.of(Map.of("period", "Last 30 days", "revenue", 1250000, "orders", 450)),
            List.of("Low stock alerts for 18 SKUs requiring immediate replenishment"),
            List.of("Expand cold storage capacity to meet growing demand in tier-2 cities"),
            List.of("Increase safety stock for top 20 high-velocity products before peak season")
        );

        OperationalAlert alert = new OperationalAlert(
            UUID.randomUUID().toString(),
            OperationalAlert.AlertType.WARNING,
            "Low Stock Alert",
            "18 products are below minimum threshold",
            "inventory_level", 50.0, 23.0,
            OffsetDateTime.now(),
            "Review reorder points and expedite pending purchase orders"
        );

        return new DashboardResponse(dashboard, List.of(insight), List.of(alert), OffsetDateTime.now());
    }

    public ReportResponse generateReport(ReportRequest request) {
        String reportId = UUID.randomUUID().toString();
        log.info("Generating report {} of type {}", request.title(), request.type());

        return new ReportResponse(
            reportId,
            request.title(),
            "/api/v1/copilot/admin/reports/" + reportId + "/download",
            OffsetDateTime.now(),
            request.format() != null ? request.format() : "PDF",
            1024
        );
    }

    public InsightResponse getInsights() {
        return new InsightResponse(
            "Business is performing well with double-digit growth across key metrics. Customer retention improved 5% month-over-month.",
            Map.of(
                "totalRevenue", 1250000,
                "totalOrders", 450,
                "activeUsers", 1250,
                "averageOrderValue", 2778,
                "growthRate", 12.5
            ),
            List.of(
                Map.of("metric", "revenue", "direction", "up", "change", 12.5, "period", "30d"),
                Map.of("metric", "orders", "direction", "up", "change", 8.3, "period", "30d"),
                Map.of("metric", "customers", "direction", "up", "change", 5.1, "period", "30d")
            ),
            List.of(
                "Expand inventory capacity for high-demand categories",
                "Launch targeted retention campaign for inactive customers",
                "Optimize pricing strategy for top 10 products"
            ),
            OffsetDateTime.now()
        );
    }

    public ForecastResponse getForecast(ForecastRequest request) {
        log.info("Generating forecast for metric={}, period={}, horizon={}", request.metric(), request.period(), request.horizon());

        Map<LocalDate, Double> forecastValues = new LinkedHashMap<>();
        LocalDate start = LocalDate.now().plusDays(1);
        for (int i = 0; i < request.horizon(); i++) {
            forecastValues.put(start.plusDays(i), 45000 + Math.random() * 5000);
        }

        ForecastResult forecast = new ForecastResult(
            request.metric(),
            request.period(),
            forecastValues,
            "95%",
            "upward",
            List.of(1.2, 0.9, 1.1, 1.3, 0.8, 1.0, 1.1)
        );

        return new ForecastResponse(
            request.metric(),
            forecast,
            "HIGH",
            List.of("Increase inventory by 15% to meet projected demand", "Adjust staffing for peak periods")
        );
    }

    public AlertResponse getAlerts() {
        List<OperationalAlert> alerts = new ArrayList<>();
        alerts.add(new OperationalAlert(
            UUID.randomUUID().toString(),
            OperationalAlert.AlertType.CRITICAL,
            "Payment Gateway Latency",
            "Payment gateway response time exceeded 5s threshold",
            "payment_latency", 2000.0, 5200.0,
            OffsetDateTime.now(),
            "Contact payment provider support and enable fallback gateway"
        ));
        alerts.add(new OperationalAlert(
            UUID.randomUUID().toString(),
            OperationalAlert.AlertType.WARNING,
            "Low Stock Alert",
            "18 products are below minimum threshold",
            "inventory_level", 50.0, 23.0,
            OffsetDateTime.now(),
            "Review reorder points and expedite pending purchase orders"
        ));
        alerts.add(new OperationalAlert(
            UUID.randomUUID().toString(),
            OperationalAlert.AlertType.INFO,
            "New Admin User Created",
            "3 new admin users were onboarded this week",
            "user_onboarding", null, 3.0,
            OffsetDateTime.now(),
            "Verify all new users have completed orientation"
        ));

        int critical = (int) alerts.stream().filter(a -> a.type() == OperationalAlert.AlertType.CRITICAL).count();
        int warning = (int) alerts.stream().filter(a -> a.type() == OperationalAlert.AlertType.WARNING).count();

        return new AlertResponse(alerts, alerts.size(), critical, warning);
    }

    public CompletableFuture<CopilotResponse> processStreamingMessage(ChatRequest request) {
        SessionId sessionId = resolveSessionId(request.sessionId());
        UserContext userContext = contextService.getUserContext(sessionId);

        PageContext pageContext = PageContext.builder()
            .pageUrl(request.pageUrl())
            .pageTitle(request.pageTitle())
            .section(request.section())
            .entityType(request.entityType())
            .entityId(request.entityId())
            .build();

        contextService.trackQuery(sessionId, request.message());
        return engine.processMessage(sessionId, request.message(), userContext, pageContext);
    }

    private SessionId resolveSessionId(String sessionIdStr) {
        if (sessionIdStr != null && !sessionIdStr.isBlank()) {
            try {
                return SessionId.fromString(sessionIdStr);
            } catch (Exception e) {
                log.warn("Invalid session ID {}, creating new session", sessionIdStr);
            }
        }
        SessionId sessionId = new SessionId();
        UserContext userContext = contextService.buildUserContext("system", "Admin", "admin@sporekart.com");
        contextService.initSession(sessionId, userContext);
        engine.startSession(sessionId, CopilotType.ADMIN, userContext);
        return sessionId;
    }
}
