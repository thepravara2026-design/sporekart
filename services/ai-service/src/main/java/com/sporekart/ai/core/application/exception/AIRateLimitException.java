package com.sporekart.ai.core.application.exception;

public class AIRateLimitException extends AIGatewayException {
    public AIRateLimitException(String module) {
        super("AI-005", "Rate limit exceeded for module: " + module);
    }
}
