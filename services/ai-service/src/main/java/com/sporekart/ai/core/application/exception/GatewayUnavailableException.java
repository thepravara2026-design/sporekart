package com.sporekart.ai.core.application.exception;

public class GatewayUnavailableException extends AIGatewayException {
    public GatewayUnavailableException() {
        super("AI-001", "AI Gateway is not available");
    }
}
