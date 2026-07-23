package com.sporekart.admin.service;

import com.sporekart.copilot.CopilotSDK;
import com.sporekart.copilot.capability.Capability;
import com.sporekart.copilot.core.CopilotEngine;
import com.sporekart.copilot.domain.CopilotType;
import com.sporekart.copilot.persona.DefaultPersonas;
import com.sporekart.copilot.persona.Persona;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminCopilotRegistrationService {

    private static final Logger log = LoggerFactory.getLogger(AdminCopilotRegistrationService.class);

    private final CopilotEngine engine;

    public AdminCopilotRegistrationService() {
        this.engine = CopilotSDK.createDefaultEngine();
    }

    @PostConstruct
    public void registerAdminCopilot() {
        Persona adminPersona = DefaultPersonas.adminPersona();
        log.info("Registering Admin Copilot with persona: {}", adminPersona.name());

        List<Capability> capabilities = buildAdminCapabilities();
        log.info("Registering {} admin capabilities", capabilities.size());

        for (Capability cap : capabilities) {
            log.debug("Registered capability: {} ({})", cap.name(), cap.id());
        }

        log.info("Admin Copilot registration complete. Engine ready: {}", engine != null);
    }

    public CopilotEngine getEngine() {
        return engine;
    }

    private List<Capability> buildAdminCapabilities() {
        return List.of(
            new Capability("biz-dashboard", "BusinessDashboard", "Real-time business performance dashboard with KPIs and trends",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("sales-summary", "SalesSummary", "Sales performance summaries across periods",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("revenue-analytics", "RevenueAnalytics", "Revenue breakdown, trends, and forecasting",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("customer-insights", "CustomerInsights", "Customer segmentation, behavior, and lifetime value analysis",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("inventory-insights", "InventoryInsights", "Inventory health, turnover, and stock alerts",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("training-analytics", "TrainingAnalytics", "Training program enrollment, completion, and effectiveness",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("order-analytics", "OrderAnalytics", "Order volume, fulfillment rates, and processing metrics",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("product-performance", "ProductPerformance", "Product-level performance, margins, and rankings",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("generate-reports", "GenerateReports", "Generate PDF, Excel, and CSV reports for any metric",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("forecast-sales", "ForecastSales", "ML-based sales forecasting with confidence intervals",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("forecast-inventory", "ForecastInventory", "Inventory demand forecasting and reorder point recommendations",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("platform-health", "PlatformHealth", "System health monitoring, uptime, and performance metrics",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("risk-alerts", "RiskAlerts", "Business risk detection, anomaly alerts, and mitigation suggestions",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("recommendation-engine", "RecommendationEngine", "AI-driven business recommendations and optimizations",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("knowledge-search", "KnowledgeSearch", "Search across admin documentation, policies, and knowledge base",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("faqs", "FAQs", "Frequently asked questions about platform operations",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object")),
            new Capability("decision-support", "DecisionSupport", "Data-driven decision support with scenario analysis",
                new CopilotType[]{CopilotType.ADMIN}, Map.of("type", "object"), Map.of("type", "object"))
        );
    }
}
