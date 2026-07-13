package com.sporekart.ai.assistant.application;

import com.sporekart.ai.assistant.api.IntentResolver;
import com.sporekart.ai.assistant.domain.AssistantIntent;
import com.sporekart.ai.assistant.domain.IntentPriority;
import com.sporekart.ai.assistant.domain.IntentResult;
import com.sporekart.ai.assistant.domain.IntentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class IntentResolverImpl implements IntentResolver {

    private static final Logger log = LoggerFactory.getLogger(IntentResolverImpl.class);

    private static final double CONFIDENCE_THRESHOLD = 0.05;
    private static final double EXACT_MATCH_WEIGHT = 1.0;
    private static final double PARTIAL_MATCH_WEIGHT = 0.5;

    private final Map<String, List<String>> intentPatterns;

    public IntentResolverImpl() {
        intentPatterns = new HashMap<>();
        intentPatterns.put("product_search", List.of("search", "find", "product", "item", "lookup", "catalog", "browse"));
        intentPatterns.put("customer_support", List.of("help", "support", "issue", "problem", "complaint", "refund", "return"));
        intentPatterns.put("order_status", List.of("order", "status", "track", "delivery", "shipping", "dispatch"));
        intentPatterns.put("inventory_check", List.of("inventory", "stock", "available", "quantity", "warehouse", "supply"));
        intentPatterns.put("pricing_info", List.of("price", "cost", "rate", "pricing", "discount", "offer", "deal"));
        intentPatterns.put("training_request", List.of("training", "learn", "course", "tutorial", "guide", "education"));
        intentPatterns.put("grower_advisory", List.of("grower", "crop", "farm", "harvest", "planting", "cultivation", "yield"));
        intentPatterns.put("marketplace_listing", List.of("marketplace", "listing", "sell", "buy", "vendor", "seller", "merchant"));
        intentPatterns.put("erp_sync", List.of("erp", "sync", "integration", "import", "export", "odoo", "sap", "oracle"));
        intentPatterns.put("analytics_report", List.of("analytics", "report", "dashboard", "metrics", "insight", "statistics", "trend"));
        intentPatterns.put("account_admin", List.of("account", "admin", "setting", "profile", "preference", "config", "manage"));
        intentPatterns.put("notification_pref", List.of("notification", "alert", "notify", "reminder", "subscribe", "unsubscribe"));
    }

    @Override
    public IntentResult resolveIntent(String userInput, List<String> availableIntents) {
        if (userInput == null || userInput.isBlank()) {
            return createFallbackResult();
        }

        String input = userInput.toLowerCase().trim();
        String bestIntent = "unknown";
        double bestScore = 0.0;
        Map<String, Object> metadata = new HashMap<>();
        List<String> matchedEntities = new ArrayList<>();
        List<String> intentsToCheck;

        if (availableIntents != null && !availableIntents.isEmpty()) {
            intentsToCheck = availableIntents;
        } else {
            intentsToCheck = new ArrayList<>(intentPatterns.keySet());
        }

        for (String intent : intentsToCheck) {
            List<String> patterns = intentPatterns.getOrDefault(intent, List.of(intent));
            double score = calculateMatchScore(input, patterns);

            if (score > bestScore) {
                bestScore = score;
                bestIntent = intent;
            }

            if (score > 0) {
                for (String pattern : patterns) {
                    if (input.contains(pattern.toLowerCase())) {
                        matchedEntities.add(pattern);
                    }
                }
            }
        }

        if (bestScore < CONFIDENCE_THRESHOLD) {
            return createFallbackResult();
        }

        metadata.put("matched_keywords", matchedEntities);
        metadata.put("input_length", input.length());

        IntentPriority priority = resolvePriority(bestScore);
        List<String> entities = matchedEntities.stream().distinct().toList();

        log.debug("Resolved intent '{}' with confidence {} for input '{}'", bestIntent, bestScore, userInput);

        return new IntentResult(bestIntent, bestScore, priority, metadata, entities, false);
    }

    @Override
    public AssistantIntent classifyIntent(UUID sessionId, String userInput) {
        IntentResult result = resolveIntent(userInput, null);
        IntentStatus status = result.confidence() >= CONFIDENCE_THRESHOLD ? IntentStatus.RESOLVED : IntentStatus.UNKNOWN;

        AssistantIntent intent = new AssistantIntent(
                UUID.randomUUID(),
                sessionId,
                userInput,
                result.intent(),
                result.confidence(),
                status,
                result.priority(),
                result.metadata(),
                result.entities(),
                status == IntentStatus.UNKNOWN ? "fallback_greeting" : null,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        log.debug("Classified intent {} for session {} with status {}", intent.resolvedIntent(), sessionId, status);
        return intent;
    }

    @Override
    public List<IntentResult> resolveMultiIntent(String userInput, List<String> availableIntents) {
        if (userInput == null || userInput.isBlank()) {
            return List.of(createFallbackResult());
        }

        String input = userInput.toLowerCase().trim();
        String[] segments = input.split("\\band\\b|[,;]");
        List<IntentResult> results = new ArrayList<>();

        if (segments.length <= 1) {
            results.add(resolveIntent(userInput, availableIntents));
            return results;
        }

        for (String segment : segments) {
            String trimmed = segment.trim();
            if (!trimmed.isEmpty()) {
                IntentResult result = resolveIntent(trimmed, availableIntents);
                if (result.confidence() >= CONFIDENCE_THRESHOLD) {
                    results.add(result);
                }
            }
        }

        if (results.isEmpty()) {
            results.add(createFallbackResult());
        }

        log.debug("Resolved {} intents from multi-intent input '{}'", results.size(), userInput);
        return results;
    }

    private double calculateMatchScore(String input, List<String> patterns) {
        double score = 0.0;
        String[] words = input.split("\\s+");

        for (String pattern : patterns) {
            String patternLower = pattern.toLowerCase();

            if (input.equals(patternLower)) {
                score += EXACT_MATCH_WEIGHT;
            } else if (input.contains(patternLower)) {
                score += PARTIAL_MATCH_WEIGHT;
            } else {
                for (String word : words) {
                    String wordLower = word.toLowerCase();
                    if (wordLower.equals(patternLower)) {
                        score += EXACT_MATCH_WEIGHT;
                    } else if (wordLower.length() > 3 && patternLower.contains(wordLower) || wordLower.contains(patternLower)) {
                        score += PARTIAL_MATCH_WEIGHT * 0.5;
                    }
                }
            }
        }

        int maxPossibleMatches = patterns.size();
        if (maxPossibleMatches > 0) {
            score = Math.min(score / maxPossibleMatches, 1.0);
        }

        return score;
    }

    private IntentPriority resolvePriority(double confidence) {
        if (confidence >= 0.9) return IntentPriority.CRITICAL;
        if (confidence >= 0.7) return IntentPriority.HIGH;
        if (confidence >= 0.5) return IntentPriority.MEDIUM;
        return IntentPriority.LOW;
    }

    private IntentResult createFallbackResult() {
        return new IntentResult(
                "unknown",
                0.0,
                IntentPriority.LOW,
                Collections.emptyMap(),
                Collections.emptyList(),
                true
        );
    }
}
