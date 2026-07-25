package com.sporekart.alert.application.engine;

import com.sporekart.alert.domain.model.*;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RiskEngine {

    private final AlertRepositoryPort repository;

    public RiskEngine(AlertRepositoryPort repository) { this.repository = repository; }

    public List<BusinessRisk> generateAllRisks() {
        List<BusinessRisk> risks = new ArrayList<>();
        risks.addAll(generateRevenueRisks());
        risks.addAll(generateInventoryRisks());
        risks.addAll(generateOperationalRisks());
        risks.addAll(generateMarketplaceRisks());
        risks.addAll(generateCustomerRisks());
        risks.addAll(generateVendorRisks());
        risks.addAll(generateTrainingRisks());
        risks.addAll(generatePlatformRisks());
        risks.addAll(generateAutomationRisks());
        risks.addAll(generateAiRisks());
        return risks;
    }

    public List<BusinessRisk> generateRisksForCategory(RiskCategory category) {
        return switch (category) {
            case REVENUE -> generateRevenueRisks();
            case INVENTORY -> generateInventoryRisks();
            case OPERATIONAL -> generateOperationalRisks();
            case MARKETPLACE -> generateMarketplaceRisks();
            case CUSTOMER -> generateCustomerRisks();
            case VENDOR -> generateVendorRisks();
            case TRAINING -> generateTrainingRisks();
            case PLATFORM -> generatePlatformRisks();
            case AUTOMATION -> generateAutomationRisks();
            case AI -> generateAiRisks();
        };
    }

    public Map<String, Object> getRiskSummary() {
        List<BusinessRisk> all = repository.findAllRisks();
        long critical = all.stream().filter(r -> r.severity() == RiskSeverity.CRITICAL).count();
        long high = all.stream().filter(r -> r.severity() == RiskSeverity.HIGH).count();
        long medium = all.stream().filter(r -> r.severity() == RiskSeverity.MEDIUM).count();
        long low = all.stream().filter(r -> r.severity() == RiskSeverity.LOW).count();
        double avgScore = all.stream().mapToDouble(BusinessRisk::riskScore).average().orElse(0);
        return Map.of("totalRisks", all.size(), "critical", critical, "high", high,
                "medium", medium, "low", low, "averageRiskScore", Math.round(avgScore * 10.0) / 10.0);
    }

    private List<BusinessRisk> revenueRisks() {
        return List.of(risk("Revenue Concentration Risk", "Top 3 products contribute 65% of revenue",
                RiskCategory.REVENUE, RiskSeverity.HIGH, "REVENUE", "Product line over-concentration", 62.0, 74.0,
                "Diversify product portfolio and reduce dependency on top products"));
    }

    private List<BusinessRisk> generateRevenueRisks() { return revenueRisks(); }
    private List<BusinessRisk> generateInventoryRisks() {
        return List.of(risk("Stockout Risk", "5 high-demand SKUs at risk of stockout",
                RiskCategory.INVENTORY, RiskSeverity.HIGH, "INVENTORY", "Potential $30K lost sales", 68.0, 80.0,
                "Implement automated reorder points and safety stock"));
    }
    private List<BusinessRisk> generateOperationalRisks() {
        return List.of(risk("Process Bottleneck Risk", "Order fulfillment bottleneck at packing stage",
                RiskCategory.OPERATIONAL, RiskSeverity.MEDIUM, "OPERATIONS", "Throughput reduced by 15%", 48.0, 55.0,
                "Automate packing workflow and add packing stations"));
    }
    private List<BusinessRisk> generateMarketplaceRisks() {
        return List.of(risk("Market Share Risk", "Competitor gaining market share in 3 categories",
                RiskCategory.MARKETPLACE, RiskSeverity.HIGH, "MARKETPLACE", "Market share down 4%", 58.0, 70.0,
                "Enhance product discovery and competitive pricing"));
    }
    private List<BusinessRisk> generateCustomerRisks() {
        return List.of(risk("Customer Satisfaction Risk", "NPS score dropped from 72 to 65",
                RiskCategory.CUSTOMER, RiskSeverity.MEDIUM, "CUSTOMERS", "Customer loyalty declining", 52.0, 60.0,
                "Implement customer feedback loop and address pain points"));
    }
    private List<BusinessRisk> generateVendorRisks() {
        return List.of(risk("Vendor Dependency Risk", "Single vendor supplies 40% of critical component",
                RiskCategory.VENDOR, RiskSeverity.CRITICAL, "VENDORS", "Supply chain disruption risk", 75.0, 88.0,
                "Source alternative vendors and build safety stock"));
    }
    private List<BusinessRisk> generateTrainingRisks() {
        return List.of(risk("Training Pipeline Risk", "Course completion rate below industry benchmark",
                RiskCategory.TRAINING, RiskSeverity.LOW, "TRAINING", "Certification pipeline slowing", 32.0, 38.0,
                "Revise curriculum and introduce gamification"));
    }
    private List<BusinessRisk> generatePlatformRisks() {
        return List.of(risk("Infrastructure Capacity Risk", "Server capacity at 82% during peak hours",
                RiskCategory.PLATFORM, RiskSeverity.HIGH, "PLATFORM", "Scaling headroom insufficient", 65.0, 76.0,
                "Provision additional capacity before next growth phase"));
    }
    private List<BusinessRisk> generateAutomationRisks() {
        return List.of(risk("Automation Coverage Risk", "Only 35% of manual processes automated",
                RiskCategory.AUTOMATION, RiskSeverity.MEDIUM, "AUTOMATION", "Operational efficiency gap", 45.0, 52.0,
                "Identify and automate top 10 manual processes by volume"));
    }
    private List<BusinessRisk> generateAiRisks() {
        return List.of(risk("AI Model Drift Risk", "Recommendation model accuracy dropped 3% this month",
                RiskCategory.AI, RiskSeverity.MEDIUM, "AI_PLATFORM", "Recommendation quality degrading", 50.0, 58.0,
                "Retrain models with latest data and monitor accuracy metrics"));
    }

    private BusinessRisk risk(String title, String desc, RiskCategory cat, RiskSeverity sev,
                               String domain, String impact, double likelihood, double score,
                               String mitigation) {
        BusinessRisk r = BusinessRisk.create(title, desc, cat, sev, domain, impact, likelihood, score, mitigation);
        return repository.saveRisk(r);
    }
}
