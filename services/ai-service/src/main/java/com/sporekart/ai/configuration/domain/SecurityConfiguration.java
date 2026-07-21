package com.sporekart.ai.configuration.domain;

import java.time.Duration;

public record SecurityConfiguration(
    boolean enabled,
    boolean rateLimiterEnabled,
    int rateLimitRequestsPerMinute,
    int rateLimitBurst,
    boolean inputValidationEnabled,
    boolean contentModerationEnabled,
    boolean auditEnabled,
    boolean encryptionEnabled,
    boolean secretManagementEnabled,
    String secretProviderType,
    Duration tokenExpiry,
    boolean corsEnabled,
    boolean csrfEnabled,
    boolean rbacEnabled,
    boolean mfaEnabled
) {
    public static SecurityConfiguration defaults() {
        return new SecurityConfiguration(
            true, true, 100, 20,
            true, true, true,
            false, true,
            "environment",
            Duration.ofHours(1),
            true, true, true, false
        );
    }
}
