package com.sporekart.ai.apiregistry.application;

public class ApiRegistryException extends RuntimeException {

    public ApiRegistryException(String message) {
        super(message);
    }

    public ApiRegistryException(String message, Throwable cause) {
        super(message, cause);
    }
}
