package com.sporekart.ai.pipeline.error;

import com.sporekart.ai.pipeline.PipelineException;

public class ErrorTranslator {

    public PipelineError translate(Throwable error) {
        if (error == null) return PipelineError.unknown("Unknown error");

        if (error instanceof PipelineException pe) {
            return translatePipelineException(pe);
        }

        var message = error.getMessage();
        if (message == null) message = error.getClass().getSimpleName();

        return classifyByMessage(message.toLowerCase());
    }

    public PipelineError translateProviderError(String provider, String rawError) {
        if (rawError == null) return PipelineError.unknown("No error details from provider");

        var lower = rawError.toLowerCase();

        if (lower.contains("auth") || lower.contains("unauthorized") || lower.contains("403")) {
            return PipelineError.authentication("Provider " + provider + " authentication failed");
        }
        if (lower.contains("rate") || lower.contains("429") || lower.contains("too many")) {
            return PipelineError.rateLimit("Provider " + provider + " rate limit exceeded");
        }
        if (lower.contains("quota") || lower.contains("insufficient") || lower.contains("402")) {
            return PipelineError.quotaExceeded("Provider " + provider + " quota exceeded");
        }
        if (lower.contains("timeout") || lower.contains("timed out") || lower.contains("504")) {
            return PipelineError.timeout("Provider " + provider + " timed out");
        }
        if (lower.contains("offline") || lower.contains("unavailable") || lower.contains("503")) {
            return PipelineError.providerOffline(provider);
        }
        if (lower.contains("model") && (lower.contains("not found") || lower.contains("invalid"))) {
            return new PipelineError("INVALID_MODEL", "Invalid model for " + provider,
                    rawError, false, PipelineError.ErrorCategory.VALIDATION);
        }
        if (lower.contains("dns") || lower.contains("connection") || lower.contains("refused")) {
            return PipelineError.network("Network error connecting to " + provider);
        }
        if (lower.contains("malformed") || lower.contains("parse") || lower.contains("unexpected")) {
            return new PipelineError("MALFORMED_RESPONSE", "Malformed response from " + provider,
                    rawError, false, PipelineError.ErrorCategory.PROVIDER);
        }

        return new PipelineError("PROVIDER_ERROR", "Provider " + provider + " error: " + rawError,
                rawError, true, PipelineError.ErrorCategory.PROVIDER);
    }

    private PipelineError translatePipelineException(PipelineException pe) {
        return switch (pe.errorCode()) {
            case VALIDATION_FAILED -> new PipelineError("VALIDATION_FAILED", pe.getMessage(),
                    null, false, PipelineError.ErrorCategory.VALIDATION);
            case AUTHENTICATION_FAILED -> PipelineError.authentication(pe.getMessage());
            case AUTHORIZATION_FAILED -> new PipelineError("FORBIDDEN", pe.getMessage(),
                    null, false, PipelineError.ErrorCategory.AUTHORIZATION);
            case RATE_LIMITED -> PipelineError.rateLimit(pe.getMessage());
            case QUOTA_EXCEEDED -> PipelineError.quotaExceeded(pe.getMessage());
            case PROVIDER_UNAVAILABLE -> PipelineError.providerOffline(pe.getMessage());
            case PROVIDER_TIMEOUT -> PipelineError.timeout(pe.getMessage());
            case RETRY_EXHAUSTED -> new PipelineError("RETRY_EXHAUSTED", pe.getMessage(),
                    null, false, PipelineError.ErrorCategory.PROVIDER);
            case TIMEOUT -> PipelineError.timeout(pe.getMessage());
            case PROVIDER_ERROR -> new PipelineError("PROVIDER_ERROR", pe.getMessage(),
                    null, true, PipelineError.ErrorCategory.PROVIDER);
            default -> PipelineError.unknown(pe.getMessage());
        };
    }

    private PipelineError classifyByMessage(String message) {
        if (message.contains("auth") || message.contains("unauthorized")) {
            return PipelineError.authentication(message);
        }
        if (message.contains("rate") || message.contains("throttle")) {
            return PipelineError.rateLimit(message);
        }
        if (message.contains("quota") || message.contains("limit")) {
            return PipelineError.quotaExceeded(message);
        }
        if (message.contains("timeout") || message.contains("timed out")) {
            return PipelineError.timeout(message);
        }
        if (message.contains("offline") || message.contains("unavailable")) {
            return new PipelineError("SERVICE_UNAVAILABLE", message, null, true,
                    PipelineError.ErrorCategory.PROVIDER);
        }
        if (message.contains("network") || message.contains("connection") || message.contains("dns")) {
            return PipelineError.network(message);
        }
        return PipelineError.internal(message);
    }
}
