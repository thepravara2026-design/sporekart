package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.domain.*;
import com.sporekart.executive.copilot.dto.RiskRequest;
import com.sporekart.executive.copilot.dto.RiskResponse;
import com.sporekart.executive.copilot.dto.RiskResponse.RiskItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RiskIntelligenceEngine {

    private static final Logger log = LoggerFactory.getLogger(RiskIntelligenceEngine.class);

    public RiskResponse assessRisks(RiskRequest request) {
        log.info("Assessing risks for category: {} severity: {}", request.category(), request.severity());
        var allRisks = generateAllRisks();
        var critical = allRisks.stream().filter(r -> r.score() >= 70).toList();
        var high = allRisks.stream().filter(r -> r.score() >= 50 && r.score() < 70).toList();
        var medium = allRisks.stream().filter(r -> r.score() >= 25 && r.score() < 50).toList();
        var low = allRisks.stream().filter(r -> r.score() < 25).toList();
        var overallScore = allRisks.stream().mapToDouble(RiskItem::score).average().orElse(0.0);
        var level = overallScore >= 70 ? "CRITICAL" : overallScore >= 50 ? "HIGH" : overallScore >= 25 ? "MEDIUM" : "LOW";
        return new RiskResponse(Math.round(overallScore * 10.0) / 10.0, level, critical, high, medium, low);
    }

    public RiskMatrix getRiskMatrix() {
        log.debug("Generating risk matrix");
        var allRisks = generateAllRisks().stream()
            .map(r -> new BusinessRisk(r.title(), r.title(), r.title(),
                BusinessRisk.RiskCategory.valueOf(r.category().toUpperCase()),
                r.severity(), r.probability(), r.score(), r.impact(),
                List.of("Mitigation 1", "Mitigation 2"), "ACTIVE"))
            .toList();
        var critical = allRisks.stream().filter(r -> r.riskScore() >= 70).toList();
        var high = allRisks.stream().filter(r -> r.riskScore() >= 50 && r.riskScore() < 70).toList();
        var medium = allRisks.stream().filter(r -> r.riskScore() >= 25 && r.riskScore() < 50).toList();
        var low = allRisks.stream().filter(r -> r.riskScore() < 25).toList();
        var overall = allRisks.stream().mapToDouble(BusinessRisk::riskScore).average().orElse(0.0);
        return new RiskMatrix(critical, high, medium, low, Math.round(overall * 10.0) / 10.0,
            overall >= 70 ? "CRITICAL" : overall >= 50 ? "HIGH" : overall >= 25 ? "MEDIUM" : "LOW");
    }

    public Map<String, Object> getTopRisks(int count) {
        log.debug("Getting top {} risks", count);
        var allRisks = generateAllRisks().stream()
            .sorted(Comparator.comparingDouble(RiskItem::score).reversed())
            .limit(count)
            .toList();
        var result = new LinkedHashMap<String, Object>();
        result.put("totalRisks", generateAllRisks().size());
        result.put("topRisks", allRisks);
        return result;
    }

    public Map<String, Object> detectRevenueDeclineRisk() {
        log.debug("Detecting revenue decline risk");
        var risk = new LinkedHashMap<String, Object>();
        risk.put("riskDetected", false);
        risk.put("currentTrend", "GROWING");
        risk.put("growthRate", 13.6);
        risk.put("warningSignals", List.of("QoQ growth rate slowing from 15% to 12%",
            "Customer acquisition cost increasing 8%", "Return rate up 1.2%"));
        risk.put("recommendation", "Monitor monthly; no immediate action needed");
        return risk;
    }

    private List<RiskItem> generateAllRisks() {
        return List.of(
            new RiskItem("Revenue Concentration", "REVENUE", 65.0, 45.0, 55.0, "Top 3 products = 62% of revenue",
                List.of("Diversify product portfolio", "Develop B2B channel", "Expand training revenue")),
            new RiskItem("Supply Chain Disruption", "SUPPLY_CHAIN", 55.0, 40.0, 45.0, "Single supplier for key substrate material",
                List.of("Identify alternative suppliers", "Build 90-day inventory buffer", "Vertical integration feasibility")),
            new RiskItem("Customer Churn", "CUSTOMER_CHURN", 45.0, 55.0, 42.5, "Repeat purchase rate declining 5%",
                List.of("Implement loyalty program", "Personalized engagement", "Win-back campaigns")),
            new RiskItem("Cash Flow Pressure", "CASH_FLOW", 40.0, 35.0, 35.0, "Receivables at 45 days",
                List.of("Improve collection cycle", "Negotiate better payment terms", "Consider invoice factoring")),
            new RiskItem("Competitive Threat", "COMPETITIVE", 50.0, 50.0, 50.0, "3 new competitors entered market",
                List.of("Strengthen brand differentiation", "Increase customer lock-in", "Patent key processes")),
            new RiskItem("Inventory Obsolescence", "INVENTORY", 35.0, 40.0, 30.0, "Spawn products have limited shelf life",
                List.of("Improve demand forecasting", "Implement FIFO strictly", "Discount near-expiry stock")),
            new RiskItem("Marketing Inefficiency", "MARKETING", 30.0, 30.0, 25.0, "CAC increased 15% YoY",
                List.of("Optimize channel mix", "Improve targeting", "A/B test creative strategies")),
            new RiskItem("Regulatory Compliance", "REGULATORY", 25.0, 20.0, 18.0, "New organic certification requirements",
                List.of("Review compliance requirements", "Update documentation", "Train quality team"))
        );
    }
}
