package com.sporekart.ai.providers.exception;

public class ProviderRateLimitException extends ProviderException {
    private final long retryAfterSeconds;

    public ProviderRateLimitException(String providerId, long retryAfterSeconds) {
        super("PROVIDER_RATE_LIMITED", "Rate limited by provider " + providerId + ". Retry after " + retryAfterSeconds + "s", 429);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public long getRetryAfterSeconds() { return retryAfterSeconds; }
}
