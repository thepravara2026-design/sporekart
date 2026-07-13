package com.sporekart.ai.content.application;

import com.sporekart.ai.content.domain.ContentGenerationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class ContentSecurityService {

    private static final Logger log = LoggerFactory.getLogger(ContentSecurityService.class);
    private static final Pattern DANGEROUS_PATTERN = Pattern.compile("[<>\"';&$`|\\\\{}()/]");
    private static final int MAX_CONTENT_LENGTH = 50000;
    private static final Set<UUID> RATE_LIMITED_USERS = ConcurrentHashMap.newKeySet();
    private final Map<UUID, UserRateLimit> rateLimits = new ConcurrentHashMap<>();

    public boolean validateInput(String content) {
        if (content == null || content.isBlank()) {
            log.warn("Input validation failed: content is empty or blank");
            return false;
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            log.warn("Input validation failed: content length {} exceeds max {}", content.length(), MAX_CONTENT_LENGTH);
            return false;
        }
        return true;
    }

    public String sanitizeContent(String content) {
        if (content == null || content.isBlank()) {
            return content;
        }
        var sanitized = DANGEROUS_PATTERN.matcher(content).replaceAll("");
        if (!sanitized.equals(content)) {
            log.warn("Content sanitized: removed dangerous characters");
        }
        return sanitized.trim();
    }

    public boolean checkRateLimit(UUID userId) {
        if (userId == null) {
            return false;
        }
        if (RATE_LIMITED_USERS.contains(userId)) {
            log.warn("Rate limit exceeded for user {}", userId);
            return false;
        }
        var limit = rateLimits.computeIfAbsent(userId, k -> new UserRateLimit(100));
        return limit.tryAcquire();
    }

    public boolean validateOutput(ContentGenerationResponse response) {
        if (response == null) {
            log.warn("Output validation failed: response is null");
            return false;
        }
        if (response.content() == null || response.content().isBlank()) {
            log.warn("Output validation failed: response content is empty");
            return false;
        }
        return true;
    }

    public boolean containsPii(String content) {
        return false;
    }

    public boolean containsProfanity(String content) {
        return false;
    }

    public void suspendUser(UUID userId) {
        RATE_LIMITED_USERS.add(userId);
        log.warn("User {} suspended", userId);
    }

    public void unsuspendUser(UUID userId) {
        RATE_LIMITED_USERS.remove(userId);
        log.info("User {} unsuspended", userId);
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
            if (now - windowStart > 60_000) {
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
