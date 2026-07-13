package com.sporekart.ai.semantic.application;

public class SemanticException extends RuntimeException {
    public SemanticException(String message) {
        super(message);
    }
    public SemanticException(String message, Throwable cause) {
        super(message, cause);
    }
}
