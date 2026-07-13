package com.sporekart.ai.assistant.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class AssistantSecurityService {

    private static final Logger log = LoggerFactory.getLogger(AssistantSecurityService.class);

    private static final List<Pattern> PROMPT_INJECTION_PATTERNS = List.of(
            Pattern.compile("ignore\\s+(all\\s+)?previous\\s+(instructions|directions|prompts)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("forget\\s+(all\\s+)?(previous|prior)\\s+(instructions|directions|prompts)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("system\\s+prompt", Pattern.CASE_INSENSITIVE),
            Pattern.compile("you\\s+are\\s+(now|not\\s+bound|free)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("override\\s+(your\\s+)?(instructions|prompt|settings)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("act\\s+as\\s+(a\\s+)?(different|new|another)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("reveal\\s+(your\\s+)?(system|internal|prompt|instructions)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("roleplay|role-play", Pattern.CASE_INSENSITIVE),
            Pattern.compile("print\\s+(your\\s+)?(prompt|instructions|system)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("output\\s+(your\\s+)?(raw|original|internal)\\s+(prompt|instructions)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("you\\s+are\\s+not\\s+(required|obligated|bound)\\s+to", Pattern.CASE_INSENSITIVE),
            Pattern.compile("disregard|ignore\\s+all|bypass", Pattern.CASE_INSENSITIVE)
    );

    private static final List<Pattern> SENSITIVE_PATTERNS = List.of(
            Pattern.compile("EMAIL", Pattern.CASE_INSENSITIVE),
            Pattern.compile("PHONE"),
            Pattern.compile("SSN"),
            Pattern.compile("CREDIT_CARD"),
            Pattern.compile("BANK_ACCOUNT"),
            Pattern.compile("AADHAAR"),
            Pattern.compile("PAN"),

            Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"),
            Pattern.compile("\\b\\d{10}\\b"),
            Pattern.compile("\\b\\d{5}-\\d{4}\\b"),
            Pattern.compile("\\b\\d{4}-\\d{4}-\\d{4}-\\d{4}\\b"),
            Pattern.compile("\\b[A-Z]{5}\\d{4}[A-Z]\\b"),
            Pattern.compile("\\b\\d{12}\\b")
    );

    private static final int DEFAULT_RATE_LIMIT = 50;
    private static final long RATE_LIMIT_WINDOW_MS = 60_000;

    private final Map<UUID, UserRateLimit> rateLimits = new ConcurrentHashMap<>();
    private final Set<UUID> suspendedUsers = ConcurrentHashMap.newKeySet();

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

    public boolean validateInput(String input) {
        if (input == null || input.isBlank()) {
            log.warn("Input validation failed: empty input");
            return false;
        }

        for (Pattern pattern : PROMPT_INJECTION_PATTERNS) {
            if (pattern.matcher(input).find()) {
                log.warn("Prompt injection detected: input matches pattern '{}'", pattern);
                return false;
            }
        }

        log.debug("Input validation passed");
        return true;
    }

    public String sanitizeOutput(String output) {
        if (output == null || output.isBlank()) {
            return output;
        }

        String sanitized = output;

        sanitized = sanitized.replaceAll("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}", "[EMAIL REDACTED]");
        sanitized = sanitized.replaceAll("\\b\\d{10}\\b", "[PHONE REDACTED]");
        sanitized = sanitized.replaceAll("\\b\\d{3}-\\d{2}-\\d{4}\\b", "[SSN REDACTED]");
        sanitized = sanitized.replaceAll("\\b\\d{4}-\\d{4}-\\d{4}-\\d{4}\\b", "[CARD REDACTED]");
        sanitized = sanitized.replaceAll("\\b\\d{12}\\b", "[ACCOUNT REDACTED]");

        if (!sanitized.equals(output)) {
            log.info("Output sanitized: sensitive data redacted");
        }

        return sanitized;
    }

    public boolean checkRateLimit(UUID userId) {
        if (userId == null) return false;
        if (suspendedUsers.contains(userId)) {
            log.warn("Rate limit check failed: user {} is suspended", userId);
            return false;
        }
        UserRateLimit limit = rateLimits.computeIfAbsent(userId, k -> new UserRateLimit(DEFAULT_RATE_LIMIT));
        boolean allowed = limit.tryAcquire();
        if (!allowed) {
            log.warn("Rate limit exceeded for user {}", userId);
        }
        return allowed;
    }

    public boolean authorizeAssistantAccess(UUID userId, UUID assistantId) {
        if (userId == null || assistantId == null) {
            log.warn("Authorization failed: null user or assistant ID");
            return false;
        }
        if (suspendedUsers.contains(userId)) {
            log.warn("Authorization failed: user {} is suspended", userId);
            return false;
        }
        log.debug("Access authorized for user {} to assistant {}", userId, assistantId);
        return true;
    }

    public List<String> detectSensitiveData(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        List<String> detected = new ArrayList<>();

        if (Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}").matcher(text).find()) {
            detected.add("EMAIL");
        }
        if (Pattern.compile("\\b\\d{10}\\b").matcher(text).find()) {
            detected.add("PHONE");
        }
        if (Pattern.compile("\\b\\d{3}-\\d{2}-\\d{4}\\b").matcher(text).find()) {
            detected.add("SSN");
        }
        if (Pattern.compile("\\b\\d{4}-\\d{4}-\\d{4}-\\d{4}\\b").matcher(text).find()) {
            detected.add("CREDIT_CARD");
        }
        if (Pattern.compile("\\b\\d{12}\\b").matcher(text).find()) {
            detected.add("BANK_ACCOUNT");
        }
        if (Pattern.compile("\\b[A-Z]{5}\\d{4}[A-Z]\\b").matcher(text).find()) {
            detected.add("PAN");
        }
        if (Pattern.compile("\\b\\d{12}\\b").matcher(text).find()) {
            detected.add("AADHAAR");
        }

        if (!detected.isEmpty()) {
            log.info("Sensitive data detected: {}", detected);
        }

        return detected;
    }

    public void suspendUser(UUID userId) {
        suspendedUsers.add(userId);
        log.warn("User {} suspended", userId);
    }

    public void unsuspendUser(UUID userId) {
        suspendedUsers.remove(userId);
        log.info("User {} unsuspended", userId);
    }

    public void resetRateLimit(UUID userId) {
        rateLimits.remove(userId);
        log.info("Rate limit reset for user {}", userId);
    }
}
