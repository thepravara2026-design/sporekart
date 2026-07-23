package com.sporekart.workspace.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class IntentDetector {

    private static final Logger log = LoggerFactory.getLogger(IntentDetector.class);

    private static final Map<String, List<String>> INTENT_KEYWORDS = new HashMap<>();

    static {
        INTENT_KEYWORDS.put("ORDER_INQUIRY", List.of("order", "purchase", "buy", "checkout", "cart", "payment", "invoice", "bill", "shipping", "delivery", "track", "refund", "cancel order"));
        INTENT_KEYWORDS.put("PRODUCT_SEARCH", List.of("search", "find", "product", "item", "catalog", "browse", "lookup", "available", "price", "cost", "offer", "deal"));
        INTENT_KEYWORDS.put("RECOMMENDATION", List.of("recommend", "suggest", "best", "top", "popular", "trending", "rating", "review", "compare"));
        INTENT_KEYWORDS.put("CULTIVATION_ADVICE", List.of("cultivat", "grow", "spawn", "substrate", "mushroom", "mycelium", "fruiting", "compost", "inoculat", "coloniz"));
        INTENT_KEYWORDS.put("DISEASE_DIAGNOSIS", List.of("disease", "infection", "mold", "contaminat", "rot", "blight", "yellow", "wilt", "spot", "lesion", "fungus", "bacteria"));
        INTENT_KEYWORDS.put("YIELD_PREDICTION", List.of("yield", "harvest", "predict", "forecast", "output", "production", "kg", "weight", "volume", "crop"));
        INTENT_KEYWORDS.put("TRAINING_INFO", List.of("training", "course", "certification", "workshop", "learn", "skill", "program", "curriculum", "module", "session"));
        INTENT_KEYWORDS.put("STUDENT_PROGRESS", List.of("student", "progress", "attendance", "grade", "score", "assessment", "result", "performance", "report card", "batch"));
        INTENT_KEYWORDS.put("LESSON_PLAN", List.of("lesson", "plan", "syllabus", "schedule", "class", "lecture", "topic", "chapter", "curriculum", "outline"));
        INTENT_KEYWORDS.put("REVENUE_REPORT", List.of("revenue", "income", "profit", "sales", "earnings", "financial", "money", "turnover", "margin", "cost"));
        INTENT_KEYWORDS.put("DASHBOARD", List.of("dashboard", "overview", "summary", "home", "main", "landing", "portal", "console"));
        INTENT_KEYWORDS.put("ANALYTICS", List.of("analytics", "insight", "trend", "statistics", "data", "metric", "kpi", "performance", "analysis", "report"));
        INTENT_KEYWORDS.put("BUSINESS_PLAN", List.of("plan", "strategy", "goal", "objective", "roadmap", "milestone", "budget", "forecast", "projection"));
        INTENT_KEYWORDS.put("WEATHER", List.of("weather", "climate", "temperature", "humidity", "rain", "forecast", "season", "environment"));
        INTENT_KEYWORDS.put("GENERAL_QUESTION", List.of("hello", "hi", "help", "what", "how", "why", "who", "where", "when", "general", "info", "about"));
    }

    public String detectIntent(String message) {
        if (message == null || message.isBlank()) {
            return "GENERAL_QUESTION";
        }
        var keywords = extractKeywords(message);
        return classifyIntentByKeywords(keywords);
    }

    public List<String> extractKeywords(String message) {
        var keywords = new ArrayList<String>();
        if (message == null || message.isBlank()) {
            return keywords;
        }
        var normalized = message.toLowerCase().replaceAll("[^a-z0-9\\s]", " ").replaceAll("\\s+", " ").trim();
        var tokens = normalized.split(" ");
        for (var token : tokens) {
            if (token.length() >= 2) {
                keywords.add(token);
            }
        }
        return keywords;
    }

    public String classifyIntentByKeywords(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return "GENERAL_QUESTION";
        }
        String bestIntent = "GENERAL_QUESTION";
        int bestScore = 0;

        for (var entry : INTENT_KEYWORDS.entrySet()) {
            var intent = entry.getKey();
            var intentKeywords = entry.getValue();
            int score = 0;
            for (var keyword : keywords) {
                for (var intentKeyword : intentKeywords) {
                    if (keyword.equals(intentKeyword) || intentKeyword.contains(keyword)) {
                        score++;
                    }
                }
            }
            if (score > bestScore) {
                bestScore = score;
                bestIntent = intent;
            }
        }
        return bestIntent;
    }

    public double getIntentConfidence(String message, String intent) {
        if (message == null || message.isBlank() || intent == null) {
            return 0.0;
        }
        var keywords = extractKeywords(message);
        var intentKeywords = INTENT_KEYWORDS.getOrDefault(intent, List.of());
        if (intentKeywords.isEmpty() || keywords.isEmpty()) {
            return 0.0;
        }
        int matches = 0;
        for (var keyword : keywords) {
            for (var intentKeyword : intentKeywords) {
                if (keyword.equals(intentKeyword) || intentKeyword.contains(keyword)) {
                    matches++;
                    break;
                }
            }
        }
        return Math.min(1.0, (double) matches / Math.max(1, keywords.size()));
    }

    public Map<String, String> extractEntities(String message, String intent) {
        var entities = new HashMap<String, String>();
        if (message == null || message.isBlank()) {
            return entities;
        }
        var lower = message.toLowerCase();
        switch (intent) {
            case "ORDER_INQUIRY" -> {
                extractOrderEntities(lower, entities);
            }
            case "PRODUCT_SEARCH" -> {
                extractProductEntities(lower, entities);
            }
            case "CULTIVATION_ADVICE", "DISEASE_DIAGNOSIS" -> {
                extractCultivationEntities(lower, entities);
            }
            case "STUDENT_PROGRESS" -> {
                extractStudentEntities(lower, entities);
            }
            case "REVENUE_REPORT", "ANALYTICS" -> {
                extractBusinessEntities(lower, entities);
            }
            default -> {
            }
        }
        return entities;
    }

    private void extractOrderEntities(String lower, Map<String, String> entities) {
        var orderPattern = Pattern.compile("order\\s*#?\\s*(\\w+)");
        var matcher = orderPattern.matcher(lower);
        if (matcher.find()) {
            entities.put("orderId", matcher.group(1));
        }
        if (lower.contains("refund")) entities.put("action", "refund");
        if (lower.contains("cancel")) entities.put("action", "cancel");
        if (lower.contains("track") || lower.contains("status")) entities.put("action", "track");
    }

    private void extractProductEntities(String lower, Map<String, String> entities) {
        var productPattern = Pattern.compile("(?:search|find|looking for|need)\\s+(?:a |an |the )?(\\w+(?:\\s+\\w+)?)");
        var matcher = productPattern.matcher(lower);
        if (matcher.find()) {
            entities.put("product", matcher.group(1));
        }
    }

    private void extractCultivationEntities(String lower, Map<String, String> entities) {
        if (lower.contains("mushroom") || lower.contains("spawn") || lower.contains("substrate")) {
            entities.put("type", "mushroom");
        }
        if (lower.contains("disease") || lower.contains("mold") || lower.contains("contaminat")) {
            entities.put("issue", "disease");
        }
        if (lower.contains("yield") || lower.contains("harvest")) {
            entities.put("action", "yield");
        }
    }

    private void extractStudentEntities(String lower, Map<String, String> entities) {
        var namePattern = Pattern.compile("student\\s+(\\w+)");
        var matcher = namePattern.matcher(lower);
        if (matcher.find()) {
            entities.put("studentName", matcher.group(1));
        }
        if (lower.contains("attendance")) entities.put("action", "attendance");
        if (lower.contains("grade") || lower.contains("score")) entities.put("action", "grade");
        if (lower.contains("progress")) entities.put("action", "progress");
    }

    private void extractBusinessEntities(String lower, Map<String, String> entities) {
        if (lower.contains("revenue") || lower.contains("income") || lower.contains("profit")) {
            entities.put("metric", "revenue");
        }
        if (lower.contains("kpi") || lower.contains("performance")) {
            entities.put("metric", "kpi");
        }
        if (lower.contains("user") || lower.contains("customer")) {
            entities.put("entity", "user");
        }
        var periodPattern = Pattern.compile("(this|last|current|previous)\\s+(week|month|quarter|year)");
        var matcher = periodPattern.matcher(lower);
        if (matcher.find()) {
            entities.put("period", matcher.group(1) + " " + matcher.group(2));
        }
    }
}
