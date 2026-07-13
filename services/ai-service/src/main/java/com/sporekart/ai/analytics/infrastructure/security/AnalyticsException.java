package com.sporekart.ai.analytics.infrastructure.security;

public class AnalyticsException extends RuntimeException {

    private final String code;

    public AnalyticsException(String code, String message) {
        super(message);
        this.code = code;
    }

    public AnalyticsException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static AnalyticsException notFound(String message) {
        return new AnalyticsException("ANL_404", message);
    }

    public static AnalyticsException badRequest(String message) {
        return new AnalyticsException("ANL_400", message);
    }

    public static AnalyticsException forbidden(String message) {
        return new AnalyticsException("ANL_403", message);
    }

    public static AnalyticsException conflict(String message) {
        return new AnalyticsException("ANL_409", message);
    }
}
