package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.*;
import com.sporekart.bi.copilot.dto.NaturalLanguageQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class NaturalLanguageAnalyticsEngine {

    private static final Logger log = LoggerFactory.getLogger(NaturalLanguageAnalyticsEngine.class);

    private static final Map<String, String> INTENT_PATTERNS = Map.ofEntries(
            Map.entry("QUERY", "show|display|get|what|how much|tell|list|find"),
            Map.entry("COMPARISON", "compare|versus|vs|difference|which"),
            Map.entry("TREND", "trend|over time|growth|change|pattern|trajectory"),
            Map.entry("FORECAST", "forecast|predict|next|future|projection|expect"),
            Map.entry("EXPLANATION", "why|reason|cause|explain|because"),
            Map.entry("RECOMMENDATION", "recommend|suggest|what should|advise|propose")
    );

    public NaturalLanguageQuery parseQuery(String naturalLanguageQuery) {
        log.info("Parsing natural language query: {}", naturalLanguageQuery);
        String intent = detectIntent(naturalLanguageQuery);
        List<String> metrics = extractMetrics(naturalLanguageQuery);
        List<String> dimensions = extractDimensions(naturalLanguageQuery);
        Map<String, Object> filters = extractFilters(naturalLanguageQuery);
        String timePeriod = extractTimePeriod(naturalLanguageQuery);
        String vizType = suggestVisualizationType(intent, metrics);

        return new NaturalLanguageQuery(
                UUID.randomUUID().toString(),
                naturalLanguageQuery,
                intent,
                "Generated query for: " + naturalLanguageQuery,
                dimensions, metrics, filters,
                vizType,
                "Query parsed with intent: " + intent + ", metrics: " + metrics + ", dimensions: " + dimensions,
                OffsetDateTime.now()
        );
    }

    public String detectIntent(String query) {
        String lower = query.toLowerCase();
        for (var entry : INTENT_PATTERNS.entrySet()) {
            Pattern p = Pattern.compile("\\b(" + entry.getValue() + ")\\b");
            if (p.matcher(lower).find()) {
                return entry.getKey();
            }
        }
        return "QUERY";
    }

    public List<String> extractMetrics(String query) {
        String lower = query.toLowerCase();
        List<String> metrics = new ArrayList<>();
        Map<String, List<String>> metricKeywords = Map.of(
                "revenue", List.of("revenue", "sales", "income", "earnings"),
                "orders", List.of("orders", "order count"),
                "customers", List.of("customers", "users", "clients"),
                "churn", List.of("churn", "attrition"),
                "conversion", List.of("conversion", "conversion rate"),
                "profit", List.of("profit", "margin", "profitability"),
                "growth", List.of("growth", "growth rate"),
                "satisfaction", List.of("satisfaction", "nps", "csat"),
                "inventory", List.of("inventory", "stock", "supply"),
                "enrollment", List.of("enrollment", "enrolment", "students", "trainees")
        );

        for (var entry : metricKeywords.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (lower.contains(keyword)) {
                    metrics.add(entry.getKey());
                    break;
                }
            }
        }
        return metrics.isEmpty() ? List.of("revenue") : metrics;
    }

    public List<String> extractDimensions(String query) {
        String lower = query.toLowerCase();
        List<String> dimensions = new ArrayList<>();
        if (lower.contains("region") || lower.contains("location") || lower.contains("city") || lower.contains("state")) {
            dimensions.add("region");
        }
        if (lower.contains("product") || lower.contains("category") || lower.contains("item")) {
            dimensions.add("product");
        }
        if (lower.contains("segment") || lower.contains("customer type") || lower.contains("cohort")) {
            dimensions.add("segment");
        }
        if (lower.contains("channel") || lower.contains("source") || lower.contains("medium")) {
            dimensions.add("channel");
        }
        if (lower.contains("trainer") || lower.contains("instructor") || lower.contains("teacher")) {
            dimensions.add("trainer");
        }
        return dimensions;
    }

    public Map<String, Object> extractFilters(String query) {
        String lower = query.toLowerCase();
        Map<String, Object> filters = new HashMap<>();

        Pattern regionP = Pattern.compile("\\b(north|south|east|west|northeast|northwest|southeast|southwest)\\b");
        var regionM = regionP.matcher(lower);
        if (regionM.find()) {
            filters.put("region", regionM.group(1));
        }

        Pattern segmentP = Pattern.compile("\\b(commercial|home|distributor|institution|hobbyist|residential)\\b");
        var segmentM = segmentP.matcher(lower);
        if (segmentM.find()) {
            filters.put("segment", segmentM.group(1));
        }

        if (lower.contains("top") || lower.contains("best")) {
            Pattern numP = Pattern.compile("\\b(\\d+)\\b");
            var numM = numP.matcher(lower);
            if (numM.find()) {
                filters.put("limit", Integer.parseInt(numM.group(1)));
            } else {
                filters.put("limit", 10);
            }
        }

        return filters;
    }

    public String extractTimePeriod(String query) {
        String lower = query.toLowerCase();
        if (lower.contains("last year") || lower.contains("annual") || lower.contains("yearly")) return "annual";
        if (lower.contains("last quarter") || lower.contains("quarterly")) return "quarterly";
        if (lower.contains("last month") || lower.contains("monthly")) return "monthly";
        if (lower.contains("last week") || lower.contains("weekly")) return "weekly";
        if (lower.contains("today") || lower.contains("daily") || lower.contains("current")) return "daily";
        if (lower.contains("yesterday")) return "yesterday";
        if (lower.contains("this year")) return "ytd";
        return "current";
    }

    public Map<String, Object> executeQuery(NaturalLanguageQuery parsedQuery) {
        log.info("Executing query with intent={}, metrics={}", parsedQuery.intent(), parsedQuery.metrics());
        Map<String, Object> results = new HashMap<>();
        results.put("intent", parsedQuery.intent());
        results.put("metrics", parsedQuery.metrics());
        results.put("dimensions", parsedQuery.dimensions());
        results.put("filters", parsedQuery.filters());
        results.put("period", extractTimePeriod(parsedQuery.originalQuery()));
        results.put("generatedAt", OffsetDateTime.now().toString());
        results.put("status", "executed");

        Map<String, Double> metricValues = new HashMap<>();
        for (String metric : parsedQuery.metrics()) {
            metricValues.put(metric, switch (metric) {
                case "revenue" -> 1250000.0;
                case "orders" -> 280.0;
                case "customers" -> 2500.0;
                case "churn" -> 12.0;
                case "conversion" -> 0.68;
                case "growth" -> 12.5;
                case "inventory" -> 15000.0;
                case "enrollment" -> 240.0;
                case "satisfaction" -> 82.0;
                default -> 0.0;
            });
        }
        results.put("values", metricValues);

        return results;
    }

    public String suggestVisualizationType(String intent, List<String> metrics) {
        return switch (intent) {
            case "COMPARISON" -> "bar";
            case "TREND" -> "line";
            case "FORECAST" -> "line";
            case "EXPLANATION" -> "pie";
            case "RECOMMENDATION" -> "scorecard";
            default -> metrics.size() <= 2 ? "kpi" : "bar";
        };
    }

    public String generateExplanation(NaturalLanguageQuery query, Map<String, Object> results) {
        String intent = query.intent();
        String metrics = String.join(", ", query.metrics());
        String dims = query.dimensions().isEmpty() ? "overall" : String.join(", ", query.dimensions());
        String period = extractTimePeriod(query.originalQuery());

        return "This " + intent.toLowerCase() + " shows " + metrics
                + " by " + dims + " for the " + period + " period. "
                + "Data indicates " + query.metrics().stream()
                .map(m -> m + ": " + results.getOrDefault("values", Map.of()))
                .collect(Collectors.joining(", "))
                + ".";
    }

    public NaturalLanguageQueryResponse answerNaturalLanguageQuery(String query) {
        NaturalLanguageQuery parsed = parseQuery(query);
        Map<String, Object> data = executeQuery(parsed);
        String explanation = generateExplanation(parsed, data);
        String vizType = parsed.visualizationType();
        List<String> followUps = generateFollowUpQuestions(query, data);

        VisualizationConfig viz = new VisualizationConfig(
                UUID.randomUUID().toString(), vizType, "Analytics: " + query,
                "NaturalLanguageQuery",
                Map.of("intent", parsed.intent(), "metrics", parsed.metrics()),
                parsed.metrics(), List.of(),
                List.of(), Map.of()
        );

        return new NaturalLanguageQueryResponse(
                parsed.intent(), explanation, data, viz,
                List.of(), followUps.isEmpty() ? "" : followUps.getFirst()
        );
    }

    public List<String> generateFollowUpQuestions(String query, Map<String, Object> results) {
        String lower = query.toLowerCase();
        List<String> questions = new ArrayList<>();

        if (!lower.contains("compare")) {
            questions.add("Would you like to compare " + String.join(" vs ", extractMetrics(query)) + " across regions?");
        }
        if (!lower.contains("trend") && !lower.contains("over time")) {
            questions.add("Would you like to see the trend over time?");
        }
        if (!lower.contains("forecast") && !lower.contains("predict")) {
            questions.add("Would you like a forecast for the next period?");
        }
        if (!lower.contains("region") && !lower.contains("location")) {
            questions.add("How does this break down by region?");
        }
        if (!lower.contains("segment")) {
            questions.add("Would you like to see this by customer segment?");
        }
        questions.add("Would you like recommendations based on this data?");

        return questions;
    }
}
