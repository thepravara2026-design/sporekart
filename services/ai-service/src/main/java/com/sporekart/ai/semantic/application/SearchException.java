package com.sporekart.ai.semantic.application;

public class SearchException extends SemanticException {
    public SearchException(String message) {
        super(message);
    }
    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }
}
