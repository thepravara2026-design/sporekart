package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.domain.NaturalLanguageQuery;
import com.sporekart.executive.copilot.dto.ExecutiveQueryRequest;
import com.sporekart.executive.copilot.dto.ExecutiveQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NaturalLanguageEngine {

    private static final Logger log = LoggerFactory.getLogger(NaturalLanguageEngine.class);

    public ExecutiveQueryResponse processQuery(ExecutiveQueryRequest request) {
        log.info("Processing executive query: intent={} query={}", request.intent(), request.query());
        var queryId = UUID.randomUUID().toString();
        Map<String, Object> data;
        List<String> insights;
        List<String> recommendations;
        String summary;

        switch (request.intent().toLowerCase()) {
            case "company_performance":
            case "how_is_company":
                data = Map.of("overallHealth", 82.5, "revenueGrowth", "13.6%", "profitMargin", "24%",
                    "customerGrowth", "18%", "topPerformer", "Training department");
                insights = List.of("Company is performing above industry average",
                    "Revenue growth accelerated 2% from last quarter",
                    "Training segment is the fastest growing department");
                recommendations = List.of("Focus on South India expansion to sustain growth",
                    "Address inventory efficiency to improve profitability");
                summary = "SporeKart is performing strongly with an overall health score of 82.5/100. "
                    + "Revenue grew 13.6% YoY with healthy profit margins of 24%. Training and growth are key strengths.";
                break;

            case "biggest_risk":
            case "risk_analysis":
                data = Map.of("topRisk", "Revenue Concentration", "riskScore", 55.0,
                    "totalRisks", 8, "criticalRisks", 0, "highRisks", 2);
                insights = List.of("Revenue concentration is the highest risk (score: 55/100)",
                    "Top 3 products contribute 62% of total revenue",
                    "Customer churn risk increasing - retention rate at 78%");
                recommendations = List.of("Diversify product portfolio aggressively",
                    "Launch B2B channel to reduce concentration",
                    "Implement customer loyalty program");
                summary = "Your biggest business risk is revenue concentration (score: 55/100). "
                    + "Your top 3 products account for 62% of revenue. Product diversification is recommended.";
                break;

            case "weekly_focus":
                data = Map.of("priority1", "Inventory clearance for dead stock",
                    "priority2", "South India market research", "priority3", "Team hiring for training");
                insights = List.of("Inventory dead stock increased to Rs. 85K",
                    "South India market shows 22% growth potential",
                    "Training department needs 2 additional trainers");
                recommendations = List.of("Launch dead stock clearance sale this week",
                    "Assign team for South India feasibility study",
                    "Start hiring process for training team");
                summary = "This week, focus on: (1) Launching inventory clearance for Rs. 85K dead stock, "
                    + "(2) Initiating South India market research, (3) Starting hiring for training team.";
                break;

            case "revenue_drop":
                data = Map.of("lastMonthRevenue", 3800000.0, "previousMonthRevenue", 3950000.0,
                    "drop", 150000.0, "dropPct", 3.8, "primaryCause", "Seasonal slowdown in spawn sales");
                insights = List.of("Revenue decreased 3.8% primarily due to seasonal spawn demand slowdown",
                    "Training revenue actually increased 12% offsetting some decline",
                    "Customer count remained stable, indicating retention is not the issue");
                recommendations = List.of("Launch targeted summer spawn promotion",
                    "Accelerate training program launches to fill revenue gap",
                    "Review marketing spend efficiency");
                summary = "Revenue dropped 3.8% (Rs. 1.5L) mainly due to seasonal slowdown in spawn sales. "
                    + "Training revenue grew 12% partially offsetting the decline. This is a seasonal pattern.";
                break;

            case "expansion":
                data = Map.of("recommendedMarket", "South India", "marketSize", "Rs. 15Cr",
                    "growthRate", "22%", "investmentRequired", 2500000.0, "expectedROI", 185.0);
                insights = List.of("South India has 22% market growth with low current penetration",
                    "Investment of Rs. 25L can yield 185% ROI over 9 months",
                    "No dominant competitor in tier-2 cities");
                recommendations = List.of("Proceed with South India warehouse setup",
                    "Hire regional sales team of 5 members",
                    "Start with online-only presence before physical expansion");
                summary = "Yes, expanding inventory and operations to South India is recommended. "
                    + "The market is growing at 22% with Rs. 15Cr opportunity and estimated 185% ROI.";
                break;

            case "training":
                data = Map.of("currentBatches", 6, "capacity", 8, "demandGrowth", 45.0,
                    "completionRate", 92.0, "revenuePerBatch", 250000.0);
                insights = List.of("Training demand growing 45% YoY",
                    "Current capacity utilization: 75% (6 of 8 batches)",
                    "Training has highest customer satisfaction score: 4.8/5");
                recommendations = List.of("Add 2 new training batches next month",
                    "Develop advanced-level curriculum for repeat students",
                    "Consider corporate training B2B offering");
                summary = "Yes, you can launch another training batch. Current capacity is at 75% with 6 active batches "
                    + "out of 8 possible. Training demand is growing 45% YoY with 92% completion rate.";
                break;

            case "profitability":
                data = Map.of("grossMargin", 40.0, "netMargin", 24.0, "operatingCost", 6500000.0,
                    "trend", "IMPROVING", "ebitdaMargin", 30.4);
                insights = List.of("Net profit margin improved from 22.7% to 24%",
                    "Operating costs grew slower than revenue (8.3% vs 13.6%)",
                    "Gross margin stable at 40% - healthy for the industry");
                recommendations = List.of("Continue operational efficiency programs",
                    "Review procurement costs for margin improvement",
                    "Consider value-added products to increase margins");
                summary = "Profitability trend is IMPROVING. Net margin increased to 24% from 22.7%, "
                    + "driven by operational efficiencies and favorable product mix. Gross margin holds at 40%.";
                break;

            default:
                data = Map.of("message", "Query received", "confidence", "Processing");
                insights = List.of("Your query has been received and is being analyzed");
                recommendations = List.of("Available intents: company_performance, biggest_risk, weekly_focus, revenue_drop, expansion, training, profitability");
                summary = "I understand you're asking about '" + request.query() + "'. "
                    + "I can help with company performance, risk analysis, strategic decisions, and more.";
        }

        return new ExecutiveQueryResponse(queryId, request.intent(), summary, data, insights, recommendations, 150L);
    }

    public NaturalLanguageQuery analyzeQuery(String query, String intent) {
        log.debug("Analyzing NL query: {}", query);
        var response = processQuery(new ExecutiveQueryRequest(query, intent, null, null, null));
        return new NaturalLanguageQuery(response.queryId(), query, intent, response.executiveSummary(),
            response.data(), response.insights(), response.recommendations());
    }
}
