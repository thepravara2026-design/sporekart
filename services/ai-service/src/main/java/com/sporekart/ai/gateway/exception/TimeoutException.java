package com.sporekart.ai.gateway.exception;

public class TimeoutException extends GatewayException {
    private final long timeoutMs;

    public TimeoutException(long timeoutMs) {
        super("TIMEOUT", "Request timed out after " + timeoutMs + "ms", 504);
        this.timeoutMs = timeoutMs;
    }

    public long getTimeoutMs() { return timeoutMs; }
}
