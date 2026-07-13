package com.sporekart.ai.workflow.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class WorkflowSecurityService {
    private static final Logger log = LoggerFactory.getLogger(WorkflowSecurityService.class);
    private static final Pattern EXPRESSION_INJECTION = Pattern.compile("[<>\"';&$`|\\\\{}()/]");
    private static final int DEFAULT_RATE_LIMIT = 50;
    private static final long RATE_LIMIT_WINDOW_MS = 60_000;
    private static final Set<String> SUSPENDED_USERS = ConcurrentHashMap.newKeySet();
    private final Map<String, UserRateLimit> rateLimits = new ConcurrentHashMap<>();

    public boolean canAccessWorkflow(String userId, UUID workflowOwnerId) {
        if (userId == null || userId.isBlank()) {
            log.warn("Access denied: missing user ID");
            return false;
        }
        if (SUSPENDED_USERS.contains(userId)) {
            log.warn("Access denied: user {} is suspended", userId);
            return false;
        }
        return userId.equals(workflowOwnerId.toString());
    }

    public boolean canExecuteWorkflow(String userId) {
        if (userId == null || userId.isBlank()) {
            log.warn("Execution denied: missing user ID");
            return false;
        }
        if (SUSPENDED_USERS.contains(userId)) {
            log.warn("Execution denied: user {} is suspended", userId);
            return false;
        }
        return true;
    }

    public boolean checkRateLimit(String userId) {
        if (userId == null) return false;
        var limit = rateLimits.computeIfAbsent(userId, k -> new UserRateLimit(DEFAULT_RATE_LIMIT));
        return limit.tryAcquire();
    }

    public String sanitizeExpression(String expr) {
        if (expr == null || expr.isBlank()) return expr;
        var sanitized = EXPRESSION_INJECTION.matcher(expr).replaceAll("");
        if (!sanitized.equals(expr)) {
            log.warn("Expression sanitized: removed special characters");
        }
        return sanitized.trim();
    }

    public boolean validateWorkflowName(String name) {
        if (name == null || name.isBlank()) return false;
        if (name.length() > 255) {
            log.warn("Workflow name too long: {} chars", name.length());
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
