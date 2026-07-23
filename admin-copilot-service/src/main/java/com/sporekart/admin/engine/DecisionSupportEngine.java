package com.sporekart.admin.engine;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DecisionSupportEngine {

    public List<Map<String, Object>> getReplenishmentRecommendations() {
        List<Map<String, Object>> recommendations = new ArrayList<>();

        recommendations.add(Map.of(
            "product", "Organic Tomatoes",
            "currentStock", 12,
            "recommendedQuantity", 200,
            "priority", "HIGH",
            "supplier", "GreenValley Farms",
            "leadTimeDays", 3
        ));

        recommendations.add(Map.of(
            "product", "Fresh Milk",
            "currentStock", 8,
            "recommendedQuantity", 100,
            "priority", "HIGH",
            "supplier", "DairyBest Co",
            "leadTimeDays", 1
        ));

        recommendations.add(Map.of(
            "product", "Wheat Flour",
            "currentStock", 5,
            "recommendedQuantity", 150,
            "priority", "MEDIUM",
            "supplier", "GrainCorp",
            "leadTimeDays", 5
        ));

        recommendations.add(Map.of(
            "product", "Cold Coffee",
            "currentStock", 45,
            "recommendedQuantity", 80,
            "priority", "LOW",
            "supplier", "BeverageHub",
            "leadTimeDays", 4
        ));

        return recommendations;
    }

    public List<Map<String, Object>> getMarketingRecommendations() {
        List<Map<String, Object>> recommendations = new ArrayList<>();

        recommendations.add(Map.of(
            "campaign", "Tier-3 City Expansion",
            "channel", "Social Media + Local Events",
            "budget", 250000,
            "expectedROI", 3.5,
            "targetAudience", "Households in tier-3 cities",
            "duration", "45 days"
        ));

        recommendations.add(Map.of(
            "campaign", "Loyalty Program Revival",
            "channel", "Email + SMS + In-App",
            "budget", 150000,
            "expectedROI", 4.2,
            "targetAudience", "Inactive customers (90+ days)",
            "duration", "30 days"
        ));

        recommendations.add(Map.of(
            "campaign", "Produce Season Promotion",
            "channel", "App Banner + Push Notification",
            "budget", 100000,
            "expectedROI", 5.0,
            "targetAudience", "All active customers",
            "duration", "14 days"
        ));

        return recommendations;
    }

    public List<Map<String, Object>> getPricingRecommendations() {
        List<Map<String, Object>> recommendations = new ArrayList<>();

        recommendations.add(Map.of(
            "product", "Organic Milk 1L",
            "currentPrice", 85,
            "suggestedPrice", 92,
            "impact", "HIGH",
            "rationale", "Price elasticity low; competitors priced at Rs. 95-100"
        ));

        recommendations.add(Map.of(
            "product", "Sourdough Bread",
            "currentPrice", 65,
            "suggestedPrice", 70,
            "impact", "MEDIUM",
            "rationale", "Premium product with loyal customer base; 8% increase justified"
        ));

        recommendations.add(Map.of(
            "product", "Mixed Nuts 200g",
            "currentPrice", 120,
            "suggestedPrice", 115,
            "impact", "LOW",
            "rationale", "Increase volume by reducing price; competitor pressure detected"
        ));

        return recommendations;
    }

    public List<Map<String, Object>> getOperationalImprovements() {
        List<Map<String, Object>> improvements = new ArrayList<>();

        improvements.add(Map.of(
            "area", "Warehouse Fulfillment",
            "suggestion", "Implement batch picking for top 50 high-velocity SKUs",
            "expectedBenefit", "35% reduction in picking time",
            "effort", "MEDIUM",
            "timeline", "2 weeks"
        ));

        improvements.add(Map.of(
            "area", "Customer Support",
            "suggestion", "Deploy AI chatbot for order status inquiries",
            "expectedBenefit", "60% reduction in support tickets for order tracking",
            "effort", "LOW",
            "timeline", "1 week"
        ));

        improvements.add(Map.of(
            "area", "Inventory Management",
            "suggestion", "Set up automated reorder alerts using ML-based demand forecasting",
            "expectedBenefit", "Reduce stockouts by 80% and excess inventory by 25%",
            "effort", "HIGH",
            "timeline", "6 weeks"
        ));

        improvements.add(Map.of(
            "area", "Delivery Logistics",
            "suggestion", "Optimize delivery routes using real-time traffic data integration",
            "expectedBenefit", "15% reduction in delivery costs and 20% faster deliveries",
            "effort", "MEDIUM",
            "timeline", "4 weeks"
        ));

        return improvements;
    }
}
