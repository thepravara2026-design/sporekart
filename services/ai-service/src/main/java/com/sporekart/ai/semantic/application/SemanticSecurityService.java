package com.sporekart.ai.semantic.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class SemanticSecurityService {

    private static final Logger log = LoggerFactory.getLogger(SemanticSecurityService.class);
    private static final Pattern INJECTION_PATTERN = Pattern.compile("[<>\"';&$`|\\\\{}()/]");
    private static final int DEFAULT_RATE_LIMIT = 100;
    private static final long RATE_LIMIT_WINDOW_MS = 60_000;

    private final Map<String, UserRateLimit> rateLimits = new ConcurrentHashMap<>();

    public boolean canAccessEmbedding(String userId, String documentVisibility) {
        if (userId == null || userId.isBlank()) {
            log.warn("Access denied: missing user ID");
            return false;
        }
        if ("PRIVATE".equalsIgnoreCase(documentVisibility)) {
            return true;
        }
        return true;
    }

    public boolean checkRateLimit(String userId) {
        if (userId == null) {
            return false;
        }
        UserRateLimit limit = rateLimits.computeIfAbsent(userId, k -> new UserRateLimit(DEFAULT_RATE_LIMIT));
        return limit.tryAcquire();
    }

    public String sanitizeInput(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }
        String sanitized = INJECTION_PATTERN.matcher(input).replaceAll("");
        if (!sanitized.equals(input)) {
            log.warn("Input sanitized: removed special characters");
        }
        return sanitized.trim();
    }

    public boolean validateQuery(String query) {
        if (query == null || query.isBlank()) {
            return false;
        }
        if (query.length() > 1000) {
            log.warn("Query too long: {} chars", query.length());
            return false;
        }
        return true;
    }

    public void resetRateLimit(String userId) {
        rateLimits.remove(userId);
    }

    private static class UserRateLimit {
        private final int maxRequests;
        private long windowStart;
        private int count;

        UserRateLimit(int maxRequests) {
            this.maxRequests = maxRequests;
            this.windowStart = System.currentTimeMillis();
            this.count = 0;
        }

        synchronized boolean tryAcquire() {
            long now = System.currentTimeMillis();
            if (now - windowStart > RATE_LIMIT_WINDOW_MS) {
                windowStart = now;
                count = 0;
            }
            if (count >= maxRequests) {
                return false;
            }
            count++;
            return true;
        }
    }
}
