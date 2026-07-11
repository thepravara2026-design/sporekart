package com.sporekart.ai.common.exception;

public class DeviceRegistrationException extends RuntimeException {
    public DeviceRegistrationException(String message) {
        super(message);
    }

    public DeviceRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}

