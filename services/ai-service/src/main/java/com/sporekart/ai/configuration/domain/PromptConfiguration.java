package com.sporekart.ai.configuration.domain;

import java.time.Duration;

public record PromptConfiguration(
    boolean enabled,
    boolean cachingEnabled,
    Duration cacheTtl,
    boolean versioningEnabled,
    boolean approvalWorkflowEnabled,
    boolean auditEnabled,
    boolean templateValidationEnabled,
    boolean injectionDetectionEnabled,
    int maxPromptLength,
    int maxTemplateVariables,
    boolean importExportEnabled
) {
    public static PromptConfiguration defaults() {
        return new PromptConfiguration(
            true, true, Duration.ofHours(1),
            true, true, true,
            true, true,
            32000, 50,
            true
        );
    }
}
