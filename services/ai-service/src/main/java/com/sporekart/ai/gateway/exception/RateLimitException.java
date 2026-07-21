package com.sporekart.ai.gateway.exception;

public class RateLimitException extends GatewayException {
    private final long retryAfterSeconds;

    public RateLimitException(long retryAfterSeconds) {
        super("RATE_LIMITED", "Rate limit exceeded. Retry after " + retryAfterSeconds + " seconds", 429);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public long getRetryAfterSeconds() { return retryAfterSeconds; }
}
