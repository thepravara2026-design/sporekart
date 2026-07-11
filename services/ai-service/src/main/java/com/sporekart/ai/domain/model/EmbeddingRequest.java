package com.sporekart.ai.domain.model;

public class EmbeddingRequest {
    private final String text;

    public EmbeddingRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
