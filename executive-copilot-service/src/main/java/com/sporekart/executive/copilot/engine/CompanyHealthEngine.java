package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.domain.*;
import com.sporekart.executive.copilot.dto.HealthResponse;
import com.sporekart.executive.copilot.dto.HealthResponse.HealthDimensionItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyHealthEngine {

    private static final Logger log = LoggerFactory.getLogger(CompanyHealthEngine.class);

    public CompanyHealth calculateOverallHealth() {
        log.info("Calculating overall company health");
        var dimensions = new LinkedHashMap<String, Double>();
        dimensions.put("revenue", 82.5);
        dimensions.put("profitability", 75.0);
        dimensions.put("growth", 88.0);
        dimensions.put("customer", 78.5);
        dimensions.put("operations", 85.0);
        dimensions.put("marketing", 80.0);
        dimensions.put("inventory", 72.0);
        dimensions.put("training", 90.0);
        var overall = dimensions.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        overall = Math.round(overall * 10.0) / 10.0;
        var riskLevel = overall >= 80 ? "LOW" : overall >= 60 ? "MODERATE" : "HIGH";
        var summary = "SporeKart is performing " + (riskLevel.equals("LOW") ? "strongly" : "moderately")
            + " with an overall health score of " + overall + "/100. "
            + "Training and growth are key strengths. Inventory and profitability need attention.";
        return new CompanyHealth(overall, 80.0, overall - 80.0, dimensions, riskLevel, summary);
    }

    public HealthResponse getHealthReport() {
        var health = calculateOverallHealth();
        var dimensions = health.dimensions().entrySet().stream()
            .map(e -> new HealthDimensionItem(e.getKey(), e.getValue(), e.getValue() - 2.0,
                e.getValue() >= 80 ? "STRONG" : e.getValue() >= 60 ? "WATCH" : "CRITICAL",
                getInsight(e.getKey(), e.getValue())))
            .toList();
        return new HealthResponse(health.overallScore(), health.previousScore(), health.trend(),
            health.riskLevel(), dimensions, health.summary());
    }

    public CompanyHealth getDimensionHealth(String dimension) {
        log.debug("Getting health for dimension: {}", dimension);
        var allHealth = calculateOverallHealth();
        var score = allHealth.dimensions().getOrDefault(dimension, 0.0);
        var singleDim = new LinkedHashMap<String, Double>();
        singleDim.put(dimension, score);
        return new CompanyHealth(score, score - 3.0, 3.0, singleDim,
            score >= 80 ? "LOW" : score >= 60 ? "MODERATE" : "HIGH",
            dimension + " health score: " + score);
    }

    public BusinessSustainability assessSustainability() {
        log.debug("Assessing business sustainability");
        return new BusinessSustainability(82.0, 65.0, 40.0, 55.0, 45.0, "LOW",
            "Business is sustainably positioned with good diversification opportunities");
    }

    public CashFlowIndicators analyzeCashFlow() {
        log.debug("Analyzing cash flow indicators");
        return new CashFlowIndicators(2500000.0, -500000.0, -200000.0, 1800000.0, 350000.0, 12.0, "HEALTHY");
    }

    private String getInsight(String dimension, double score) {
        if (score >= 85) return "Excellent " + dimension + " performance - above industry benchmark";
        if (score >= 70) return "Good " + dimension + " performance - minor improvements possible";
        if (score >= 50) return dimension.substring(0, 1).toUpperCase() + dimension.substring(1) + " needs attention";
        return "Critical " + dimension + " issues require immediate action";
    }
}
