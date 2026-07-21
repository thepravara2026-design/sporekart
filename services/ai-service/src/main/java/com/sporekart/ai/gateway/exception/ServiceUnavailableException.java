package com.sporekart.ai.gateway.exception;

public class ServiceUnavailableException extends GatewayException {
    public ServiceUnavailableException(String service) {
        super("SERVICE_UNAVAILABLE", service + " is currently unavailable", 503);
    }
}
