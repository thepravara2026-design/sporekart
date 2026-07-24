package com.sporekart.platform.observability.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SecurityMetrics {

    private static final Logger log = LoggerFactory.getLogger(SecurityMetrics.class);
    private final MeterRegistry registry;

    private final Counter failedLogins;
    private final Counter permissionDenied;
    private final Counter jwtErrors;
    private final Counter secretAccess;
    private final Counter promptInjections;
    private final Counter aiAbuseAttempts;
    private final Counter rateLimitTriggers;
    private final Counter pluginViolations;
    private final Counter unauthorizedAccess;
    private final Counter suspiciousActivity;

    public SecurityMetrics(MeterRegistry registry) {
        this.registry = registry;

        this.failedLogins = Counter.builder("sporekart.security.auth.failed_logins")
            .description("Failed login attempts").register(registry);
        this.permissionDenied = Counter.builder("sporekart.security.auth.permission_denied")
            .description("Permission denied events").register(registry);
        this.jwtErrors = Counter.builder("sporekart.security.auth.jwt_errors")
            .description("JWT validation errors").register(registry);
        this.secretAccess = Counter.builder("sporekart.security.secrets.access")
            .description("Secret access events").register(registry);
        this.promptInjections = Counter.builder("sporekart.security.ai.prompt_injections")
            .description("Prompt injection attempts").register(registry);
        this.aiAbuseAttempts = Counter.builder("sporekart.security.ai.abuse_attempts")
            .description("AI abuse detection events").register(registry);
        this.rateLimitTriggers = Counter.builder("sporekart.security.ratelimit.triggers")
            .description("Rate limit triggered events").register(registry);
        this.pluginViolations = Counter.builder("sporekart.security.plugin.violations")
            .description("Plugin policy violations").register(registry);
        this.unauthorizedAccess = Counter.builder("sporekart.security.unauthorized.access")
            .description("Unauthorized access attempts").register(registry);
        this.suspiciousActivity = Counter.builder("sporekart.security.suspicious.activity")
            .description("Suspicious activity detected").register(registry);

        log.info("Security metrics initialized: 10 counters");
    }

    public void recordFailedLogin(String username) {
        Counter.builder("sporekart.security.auth.failed_logins")
            .tag("username", username).register(registry).increment();
        failedLogins.increment();
    }

    public void recordPermissionDenied(String resource) {
        Counter.builder("sporekart.security.auth.permission_denied")
            .tag("resource", resource).register(registry).increment();
        permissionDenied.increment();
    }

    public void recordJwtError(String error) {
        Counter.builder("sporekart.security.auth.jwt_errors")
            .tag("error", error).register(registry).increment();
        jwtErrors.increment();
    }

    public void recordSecretAccess(String secret) {
        Counter.builder("sporekart.security.secrets.access")
            .tag("secret", secret).register(registry).increment();
        secretAccess.increment();
    }

    public void recordPromptInjection(String source) {
        Counter.builder("sporekart.security.ai.prompt_injections")
            .tag("source", source).register(registry).increment();
        promptInjections.increment();
    }

    public void recordAiAbuseAttempt(String pattern) {
        Counter.builder("sporekart.security.ai.abuse_attempts")
            .tag("pattern", pattern).register(registry).increment();
        aiAbuseAttempts.increment();
    }

    public void recordRateLimitTrigger(String endpoint) {
        Counter.builder("sporekart.security.ratelimit.triggers")
            .tag("endpoint", endpoint).register(registry).increment();
        rateLimitTriggers.increment();
    }

    public void recordPluginViolation(String plugin) {
        Counter.builder("sporekart.security.plugin.violations")
            .tag("plugin", plugin).register(registry).increment();
        pluginViolations.increment();
    }

    public void recordUnauthorizedAccess(String ip) {
        Counter.builder("sporekart.security.unauthorized.access")
            .tag("ip", ip).register(registry).increment();
        unauthorizedAccess.increment();
    }

    public void recordSuspiciousActivity(String type) {
        Counter.builder("sporekart.security.suspicious.activity")
            .tag("type", type).register(registry).increment();
        suspiciousActivity.increment();
    }
}
