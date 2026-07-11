package com.sporekart.ai.prompt.application;

public class PromptNotFoundException extends RuntimeException {
    public PromptNotFoundException(String message) {
        super(message);
    }
}
