package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.domain.StrategicRecommendation;
import com.sporekart.executive.copilot.domain.StrategicRecommendation.RecommendationCategory;
import com.sporekart.executive.copilot.dto.DecisionRequest;
import com.sporekart.executive.copilot.dto.DecisionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StrategicDecisionEngine {

    private static final Logger log = LoggerFactory.getLogger(StrategicDecisionEngine.class);

    public DecisionResponse getRecommendation(DecisionRequest request) {
        log.info("Getting strategic recommendation for intent: {} category: {}", request.intent(), request.category());
        var recommendation = switch (request.intent().toLowerCase()) {
            case "expansion" -> Map.of(
                "rec", "Expand operations to South India market with dedicated warehouse and distribution network",
                "roi", 185.0, "impact", 92.0, "confidence", 0.85, "risk", "MEDIUM",
                "timeframe", "6-9 months",
                "data", List.of("South India market growing at 22% YoY", "Current penetration only 15%", "Competitor presence weak in tier-2 cities"),
                "alternatives", List.of("Start with online-only expansion", "Partner with local distributor first")
            );
            case "market_entry" -> Map.of(
                "rec", "Enter UAE and GCC markets through strategic e-commerce partnership",
                "roi", 220.0, "impact", 78.0, "confidence", 0.70, "risk", "HIGH",
                "timeframe", "12-18 months",
                "data", List.of("UAE mushroom market growing at 18%", "Premium pricing possible (2x India)", "Regulatory compliance requirements"),
                "alternatives", List.of("License brand to local distributor", "Focus on B2B export first")
            );
            case "pricing" -> Map.of(
                "rec", "Optimize pricing strategy: increase premium kit prices by 8%, introduce value bundle for substrates",
                "roi", 45.0, "impact", 65.0, "confidence", 0.88, "risk", "LOW",
                "timeframe", "1-2 months",
                "data", List.of("Price elasticity analysis shows room for 8% increase on premium", "Bundling can increase AOV by 15%"),
                "alternatives", List.of("Flat 5% increase across all products", "Tiered pricing by volume")
            );
            case "investment" -> Map.of(
                "rec", "Invest 2.5Cr in automated warehouse and inventory management system",
                "roi", 150.0, "impact", 85.0, "confidence", 0.82, "risk", "MEDIUM",
                "timeframe", "4-6 months",
                "data", List.of("Warehouse efficiency can improve 35%", "Labor cost reduction of 20%", "ROI breakeven in 14 months"),
                "alternatives", List.of("Phased implementation over 12 months", "Lease automated equipment instead")
            );
            default -> Map.of(
                "rec", "Maintain current strategic focus with incremental improvements across operations",
                "roi", 25.0, "impact", 40.0, "confidence", 0.92, "risk", "LOW",
                "timeframe", "Ongoing",
                "data", List.of("Current strategy delivering 15% YoY growth", "No major market shifts detected"),
                "alternatives", List.of("Accelerate digital marketing investment", "Explore strategic partnerships")
            );
        };
        return new DecisionResponse(
            (String) recommendation.get("rec"),
            (Double) recommendation.get("roi"),
            (Double) recommendation.get("impact"),
            (Double) recommendation.get("confidence"),
            (String) recommendation.get("risk"),
            (String) recommendation.get("timeframe"),
            (List<String>) recommendation.get("data"),
            (List<String>) recommendation.get("alternatives")
        );
    }

    public StrategicRecommendation createRecommendation(String title, String description, RecommendationCategory category,
                                                         double roi, double impact, double confidence) {
        log.debug("Creating strategic recommendation: {}", title);
        return new StrategicRecommendation(UUID.randomUUID().toString(), title, description, category,
            roi, impact, confidence, "3-6 months", List.of("Market analysis", "Financial projections"),
            "MEDIUM", List.of("Action item 1", "Action item 2"));
    }

    public List<StrategicRecommendation> prioritizeRecommendations(List<StrategicRecommendation> recs) {
        if (recs == null) return List.of();
        return recs.stream()
            .sorted((a, b) -> Double.compare(b.expectedROI() * b.confidenceScore(), a.expectedROI() * a.confidenceScore()))
            .toList();
    }

    public Map<String, Object> simulateDecisionImpact(String decision, double investment) {
        log.info("Simulating decision impact: {} investment: {}", decision, investment);
        var impact = new LinkedHashMap<String, Object>();
        impact.put("decision", decision);
        impact.put("investment", investment);
        impact.put("projectedRevenueIncrease", investment * 2.5);
        impact.put("breakevenMonths", 14);
        impact.put("confidenceInterval", "75-85%");
        impact.put("downsideRisk", "20% probability of 10% lower returns");
        return impact;
    }
}
