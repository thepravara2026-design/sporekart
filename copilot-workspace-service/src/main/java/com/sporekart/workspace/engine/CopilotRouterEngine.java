package com.sporekart.workspace.engine;

import com.sporekart.workspace.domain.RoutedMessage;
import com.sporekart.workspace.domain.RoutingRule;
import com.sporekart.workspace.dto.RoutingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class CopilotRouterEngine {

    private static final Logger log = LoggerFactory.getLogger(CopilotRouterEngine.class);

    private static final double CONFIDENCE_THRESHOLD = 0.3;
    private static final double RECENT_COPILOT_BONUS = 0.15;
    private static final double CONTEXT_KEYWORD_WEIGHT = 2.0;

    private static final Map<String, List<String>> COPILOT_KEYWORDS = new HashMap<>();

    static {
        COPILOT_KEYWORDS.put("CUSTOMER", List.of("order", "search", "checkout", "cart", "buy", "purchase", "payment", "shipping", "delivery", "track", "refund", "cancel", "price", "cost", "product", "item", "catalog"));
        COPILOT_KEYWORDS.put("ADMIN", List.of("revenue", "report", "dashboard", "analytics", "user", "kpi", "overview", "summary", "financial", "income", "profit", "sales", "metric", "stat", "insight"));
        COPILOT_KEYWORDS.put("TRAINER", List.of("lesson", "student", "attendance", "assessment", "batch", "training", "certification", "course", "class", "lecture", "curriculum", "syllabus", "grade", "score", "progress"));
        COPILOT_KEYWORDS.put("GROWER", List.of("cultivat", "spawn", "substrate", "disease", "yield", "harvest", "mushroom", "grow", "mycelium", "fruiting", "compost", "inoculat", "contaminat", "mold", "humidity", "temperature"));
    }

    private final List<RoutingRule> routingRules = new CopyOnWriteArrayList<>();
    private final Map<String, String> sessionCopilotMap = new ConcurrentHashMap<>();
    private final List<RoutedMessage> routingHistory = new CopyOnWriteArrayList<>();

    public CopilotRouterEngine() {
        initializeDefaultRules();
    }

    private void initializeDefaultRules() {
        for (var entry : COPILOT_KEYWORDS.entrySet()) {
            var rule = new RoutingRule(
                "rule-" + entry.getKey().toLowerCase(),
                String.join("|", entry.getValue()),
                entry.getKey(),
                0.3,
                10,
                List.of(),
                "ADMIN",
                false,
                List.of()
            );
            routingRules.add(rule);
        }
    }

    public RoutingResponse routeMessage(String message, String sessionId, Map<String, Object> context) {
        var ctx = context != null ? new HashMap<>(context) : new HashMap<String, Object>();
        ctx.put("sessionId", sessionId);
        return routeMessage(message, List.of("CUSTOMER", "ADMIN", "TRAINER", "GROWER"), ctx);
    }

    public RoutingResponse routeMessage(String message, List<String> availableCopilotIds, Map<String, Object> context) {
        var contextSafe = context != null ? context : Map.<String, Object>of();

        var scores = new HashMap<String, Double>();
        for (var copilotId : availableCopilotIds) {
            var confidence = calculateConfidence(message, copilotId);
            scores.put(copilotId, confidence);
        }

        var recentCopilot = (String) contextSafe.get("recentCopilot");
        if (recentCopilot != null && scores.containsKey(recentCopilot)) {
            scores.merge(recentCopilot, RECENT_COPILOT_BONUS, Double::sum);
        }

        var bestCopilot = "ADMIN";
        var bestScore = 0.0;
        for (var entry : scores.entrySet()) {
            if (entry.getValue() > bestScore) {
                bestScore = entry.getValue();
                bestCopilot = entry.getKey();
            }
        }

        var requiresHandoff = false;
        var collaboratingCopilots = new ArrayList<String>();
        var reasoning = new StringBuilder();

        if (bestScore >= CONFIDENCE_THRESHOLD) {
            reasoning.append("Routed to ").append(bestCopilot).append(" with confidence ").append(String.format("%.2f", bestScore));
            var matchedRule = findMatchingRule(bestCopilot);
            if (matchedRule != null && matchedRule.requiresCollaboration()) {
                collaboratingCopilots.addAll(matchedRule.collaboratingCopilotIds());
                reasoning.append(", collaboration with ").append(String.join(", ", matchedRule.collaboratingCopilotIds()));
            }
        } else {
            bestCopilot = "ADMIN";
            reasoning.append("No copilot met confidence threshold, fell back to ADMIN");
        }

        var sessionId = (String) contextSafe.get("sessionId");
        if (sessionId != null) {
            sessionCopilotMap.put(sessionId, bestCopilot);
        }

        var routedMessage = new RoutedMessage(
            UUID.randomUUID().toString(),
            message,
            bestCopilot,
            bestScore,
            reasoning.toString(),
            (String) contextSafe.get("currentCopilot"),
            requiresHandoff,
            List.of()
        );
        routingHistory.add(routedMessage);

        log.info("Routed message to {} with confidence {}. Reason: {}", bestCopilot, String.format("%.2f", bestScore), reasoning);

        return new RoutingResponse(bestCopilot, bestScore, reasoning.toString(), requiresHandoff, List.of());
    }

    public String detectIntent(String message) {
        if (message == null || message.isBlank()) {
            return "GENERAL_QUESTION";
        }
        var lower = message.toLowerCase();
        for (var entry : COPILOT_KEYWORDS.entrySet()) {
            for (var keyword : entry.getValue()) {
                if (lower.contains(keyword)) {
                    return entry.getKey();
                }
            }
        }
        return "GENERAL_QUESTION";
    }

    public double calculateConfidence(String message, String copilotType) {
        if (message == null || message.isBlank() || copilotType == null) {
            return 0.0;
        }
        var keywords = COPILOT_KEYWORDS.get(copilotType);
        if (keywords == null || keywords.isEmpty()) {
            return 0.0;
        }
        var lower = message.toLowerCase();
        var tokens = lower.split("\\s+");
        int matches = 0;
        for (var token : tokens) {
            for (var keyword : keywords) {
                if (token.equals(keyword) || keyword.contains(token)) {
                    matches++;
                    break;
                }
            }
        }
        return Math.min(1.0, (double) matches / Math.max(1, tokens.length));
    }

    public List<RoutingRule> getRoutingRules() {
        return List.copyOf(routingRules);
    }

    public void addRoutingRule(RoutingRule rule) {
        if (rule != null) {
            routingRules.add(rule);
            log.info("Added routing rule: {}", rule.ruleId());
        }
    }

    public void removeRoutingRule(String ruleId) {
        routingRules.removeIf(rule -> rule.ruleId().equals(ruleId));
        log.info("Removed routing rule: {}", ruleId);
    }

    private RoutingRule findMatchingRule(String copilotId) {
        return routingRules.stream()
            .filter(rule -> rule.targetCopilotId().equals(copilotId))
            .findFirst()
            .orElse(null);
    }
}
