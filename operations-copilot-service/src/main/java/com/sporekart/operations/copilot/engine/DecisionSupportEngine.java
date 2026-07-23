package com.sporekart.operations.copilot.engine;

import com.sporekart.operations.copilot.domain.DecisionRecommendation;
import com.sporekart.operations.copilot.domain.DecisionRecommendation.DecisionCategory;
import com.sporekart.operations.copilot.dto.DecisionSupportRequest;
import com.sporekart.operations.copilot.dto.DecisionSupportResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DecisionSupportEngine {

    private static final Logger log = LoggerFactory.getLogger(DecisionSupportEngine.class);

    public DecisionSupportResponse getRecommendation(DecisionSupportRequest request) {
        log.info("Getting decision recommendation for intent: {} category: {}", request.intent(), request.category());
        var recommendation = switch (request.intent().toLowerCase()) {
            case "increase_procurement" -> Map.of(
                "recommendation", "Increase procurement of " + (request.sku() != null ? request.sku() : "high-demand items") + " by 30%",
                "impact", 85.0, "cost", 150000.0, "confidence", 0.88, "risk", "LOW",
                "data", List.of("Demand forecast shows 25% growth", "Current stock covers only 15 days", "Supplier capacity available"),
                "alternatives", List.of("Consider partial increase of 15%", "Explore alternative suppliers")
            );
            case "reduce_procurement" -> Map.of(
                "recommendation", "Reduce procurement of slow-moving items by 20%",
                "impact", 45.0, "cost", 5000.0, "confidence", 0.82, "risk", "LOW",
                "data", List.of("Slow-moving inventory increased 12%", "Holding cost rising", "Demand declining 8% YoY"),
                "alternatives", List.of("Run promotion to clear stock", "Bundle with popular items")
            );
            case "transfer_inventory" -> Map.of(
                "recommendation", "Transfer 500 units of MUSH-001 from WH-MAIN to WH-NORTH",
                "impact", 65.0, "cost", 12000.0, "confidence", 0.75, "risk", "MEDIUM",
                "data", List.of("WH-NORTH running low on MUSH-001", "WH-MAIN has 3 months of stock", "Transfer cost: 12000 INR"),
                "alternatives", List.of("Send partial transfer of 250 units", "Direct supplier delivery to WH-NORTH")
            );
            case "launch_promotions" -> Map.of(
                "recommendation", "Launch 20% discount on slow-moving substrate products",
                "impact", 55.0, "cost", 25000.0, "confidence", 0.70, "risk", "MEDIUM",
                "data", List.of("Dead stock valued at 85000 INR", "Storage space needed for new arrivals"),
                "alternatives", List.of("Bundle with popular products", "B2B bulk discount")
            );
            default -> Map.of(
                "recommendation", "Maintain current operations with standard inventory levels",
                "impact", 30.0, "cost", 0.0, "confidence", 0.90, "risk", "LOW",
                "data", List.of("All KPIs within normal range", "No critical alerts active"),
                "alternatives", List.of("Review again next week", "Monitor inventory turnover")
            );
        };
        return new DecisionSupportResponse(
            (String) recommendation.get("recommendation"),
            (Double) recommendation.get("impact"),
            (Double) recommendation.get("cost"),
            (Double) recommendation.get("confidence"),
            (String) recommendation.get("risk"),
            (List<String>) recommendation.get("data"),
            (List<String>) recommendation.get("alternatives")
        );
    }

    public DecisionRecommendation createRecommendation(String title, String description, DecisionCategory category,
                                                        double impact, double cost, double confidence) {
        log.debug("Creating recommendation: {}", title);
        return new DecisionRecommendation(UUID.randomUUID().toString(), title, description, category,
            impact, cost, confidence, List.of("Analytics data"), "LOW", "immediate");
    }

    public List<DecisionRecommendation> prioritizeRecommendations(List<DecisionRecommendation> recs) {
        if (recs == null) return List.of();
        return recs.stream()
            .sorted((a, b) -> Double.compare(b.projectedImpact() * b.confidenceScore(), a.projectedImpact() * a.confidenceScore()))
            .toList();
    }

    public Map<String, Object> simulateDecision(String decision, Map<String, Object> params) {
        log.info("Simulating decision: {}", decision);
        var result = new LinkedHashMap<String, Object>();
        result.put("decision", decision);
        result.put("expectedOutcome", "POSITIVE");
        result.put("revenueImpact", 250000.0);
        result.put("costImpact", -75000.0);
        result.put("netBenefit", 175000.0);
        result.put("confidenceInterval", "75-85%");
        result.put("risks", List.of("Implementation delay could reduce benefits by 20%", "Supplier capacity constraint"));
        return result;
    }
}
