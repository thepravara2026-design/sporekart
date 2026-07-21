package com.sporekart.ai.pipeline.error;

public record PipelineError(
        String code,
        String message,
        String providerError,
        boolean retryable,
        ErrorCategory category) {

    public enum ErrorCategory {
        AUTHENTICATION,
        AUTHORIZATION,
        VALIDATION,
        RATE_LIMIT,
        QUOTA,
        PROVIDER,
        TIMEOUT,
        NETWORK,
        INTERNAL,
        UNKNOWN
    }

    public static PipelineError authentication(String message) {
        return new PipelineError("AUTH_ERROR", message, null, false, ErrorCategory.AUTHENTICATION);
    }

    public static PipelineError rateLimit(String message) {
        return new PipelineError("RATE_LIMIT", message, null, true, ErrorCategory.RATE_LIMIT);
    }

    public static PipelineError quotaExceeded(String message) {
        return new PipelineError("QUOTA_EXCEEDED", message, null, true, ErrorCategory.QUOTA);
    }

    public static PipelineError providerOffline(String provider) {
        return new PipelineError("PROVIDER_OFFLINE", "Provider is offline: " + provider,
                null, true, ErrorCategory.PROVIDER);
    }

    public static PipelineError timeout(String message) {
        return new PipelineError("TIMEOUT", message, null, true, ErrorCategory.TIMEOUT);
    }

    public static PipelineError network(String message) {
        return new PipelineError("NETWORK_ERROR", message, null, true, ErrorCategory.NETWORK);
    }

    public static PipelineError internal(String message) {
        return new PipelineError("INTERNAL_ERROR", message, null, false, ErrorCategory.INTERNAL);
    }

    public static PipelineError unknown(String message) {
        return new PipelineError("UNKNOWN_ERROR", message, null, false, ErrorCategory.UNKNOWN);
    }
}
