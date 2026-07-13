package com.sporekart.ai.conversation.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class ConversationSecurityService {

    private static final Logger log = LoggerFactory.getLogger(ConversationSecurityService.class);
    private static final Pattern INJECTION_PATTERN = Pattern.compile("[<>\"';&$`|\\\\{}()/]");
    private static final int DEFAULT_RATE_LIMIT = 100;
    private static final long RATE_LIMIT_WINDOW_MS = 60_000;

    private static final Set<String> SUSPENDED_USERS = ConcurrentHashMap.newKeySet();
    private final Map<String, UserRateLimit> rateLimits = new ConcurrentHashMap<>();

    public boolean canAccessSession(String userId, String sessionOwnerId) {
        if (userId == null || userId.isBlank()) {
            log.warn("Access denied: missing user ID");
            return false;
        }
        if (SUSPENDED_USERS.contains(userId)) {
            log.warn("Access denied: user {} is suspended", userId);
            return false;
        }
        if (userId.equals(sessionOwnerId)) {
            return true;
        }
        log.warn("Access denied: user {} cannot access session of user {}", userId, sessionOwnerId);
        return false;
    }

    public boolean checkRateLimit(String userId) {
        if (userId == null) return false;
        var limit = rateLimits.computeIfAbsent(userId, k -> new UserRateLimit(DEFAULT_RATE_LIMIT));
        return limit.tryAcquire();
    }

    public String sanitizeMessage(String content) {
        if (content == null || content.isBlank()) return content;
        var sanitized = INJECTION_PATTERN.matcher(content).replaceAll("");
        if (!sanitized.equals(content)) {
            log.warn("Message sanitized: removed special characters");
        }
        return sanitized.trim();
    }

    public boolean validateMessage(String content) {
        if (content == null || content.isBlank()) return false;
        if (content.length() > 10000) {
            log.warn("Message too long: {} chars", content.length());
            return false;
        }
        return true;
    }

    public void suspendUser(String userId) {
        SUSPENDED_USERS.add(userId);
        log.warn("User {} suspended", userId);
    }

    public void unsuspendUser(String userId) {
        SUSPENDED_USERS.remove(userId);
        log.info("User {} unsuspended", userId);
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
            if (count >= maxRequests) return false;
            count++;
            return true;
        }
    }
}
