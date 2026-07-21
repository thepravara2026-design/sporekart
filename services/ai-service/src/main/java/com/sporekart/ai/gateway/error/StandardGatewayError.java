package com.sporekart.ai.gateway.error;

public record StandardGatewayError(
    String errorCode,
    String message,
    int statusCode,
    String providerId,
    String failureType,
    boolean retryable,
    Long retryAfterSeconds
) {
    public static StandardGatewayError providerUnavailable(String providerId) {
        return new StandardGatewayError("PROVIDER_UNAVAILABLE",
            "Provider " + providerId + " is unavailable", 503,
            providerId, "UNAVAILABLE", true, null);
    }

    public static StandardGatewayError providerTimeout(String providerId) {
        return new StandardGatewayError("PROVIDER_TIMEOUT",
            "Provider " + providerId + " timed out", 504,
            providerId, "TIMEOUT", true, null);
    }

    public static StandardGatewayError rateLimited(String providerId, Long retryAfter) {
        return new StandardGatewayError("RATE_LIMITED",
            "Provider " + providerId + " rate limit exceeded", 429,
            providerId, "RATE_LIMIT", true, retryAfter);
    }

    public static StandardGatewayError invalidAuth(String providerId) {
        return new StandardGatewayError("INVALID_AUTH",
            "Invalid authentication for provider " + providerId, 401,
            providerId, "AUTH", false, null);
    }

    public static StandardGatewayError quotaExceeded(String providerId) {
        return new StandardGatewayError("QUOTA_EXCEEDED",
            "Provider " + providerId + " quota exceeded", 429,
            providerId, "QUOTA", true, null);
    }

    public static StandardGatewayError malformedResponse(String providerId) {
        return new StandardGatewayError("MALFORMED_RESPONSE",
            "Provider " + providerId + " returned malformed response", 502,
            providerId, "RESPONSE", false, null);
    }

    public static StandardGatewayError partialResponse(String providerId) {
        return new StandardGatewayError("PARTIAL_RESPONSE",
            "Provider " + providerId + " returned partial response", 502,
            providerId, "RESPONSE", true, null);
    }

    public static StandardGatewayError connectionRefused(String providerId) {
        return new StandardGatewayError("CONNECTION_REFUSED",
            "Connection refused by provider " + providerId, 503,
            providerId, "CONNECTION", true, null);
    }

    public static StandardGatewayError tlsFailure(String providerId) {
        return new StandardGatewayError("TLS_FAILURE",
            "TLS handshake failed for provider " + providerId, 502,
            providerId, "TLS", false, null);
    }

    public static StandardGatewayError dnsFailure(String providerId) {
        return new StandardGatewayError("DNS_FAILURE",
            "DNS resolution failed for provider " + providerId, 503,
            providerId, "DNS", true, null);
    }

    public static StandardGatewayError circuitOpen(String providerId) {
        return new StandardGatewayError("CIRCUIT_OPEN",
            "Circuit breaker is open for provider " + providerId, 503,
            providerId, "CIRCUIT", true, null);
    }

    public boolean isClientError() {
        return statusCode >= 400 && statusCode < 500;
    }

    public boolean isServerError() {
        return statusCode >= 500;
    }
}
