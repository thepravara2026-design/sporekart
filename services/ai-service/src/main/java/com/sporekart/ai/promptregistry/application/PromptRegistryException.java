package com.sporekart.ai.promptregistry.application;

public class PromptRegistryException extends RuntimeException {

    public PromptRegistryException(String message) {
        super(message);
    }

    public PromptRegistryException(String message, Throwable cause) {
        super(message, cause);
    }
}
