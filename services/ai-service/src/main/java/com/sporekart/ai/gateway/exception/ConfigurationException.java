package com.sporekart.ai.gateway.exception;

public class ConfigurationException extends GatewayException {
    public ConfigurationException(String message) {
        super("CONFIGURATION_ERROR", message, 500);
    }
}
