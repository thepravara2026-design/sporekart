package com.sporekart.ai.semantic.application;

public class EmbeddingException extends SemanticException {
    public EmbeddingException(String message) {
        super(message);
    }
    public EmbeddingException(String message, Throwable cause) {
        super(message, cause);
    }
}
