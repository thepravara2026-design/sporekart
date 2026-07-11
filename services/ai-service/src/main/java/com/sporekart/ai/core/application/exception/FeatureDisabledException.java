package com.sporekart.ai.core.application.exception;

public class FeatureDisabledException extends AIGatewayException {
    public FeatureDisabledException(String featureName) {
        super("AI-002", "AI feature is disabled: " + featureName);
    }
}
