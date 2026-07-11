package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class EmbeddingResponse {
    @Schema(description = "Generation status", example = "generated")
    private String status;

    @Schema(description = "Embedding string or id")
    private String embedding;

    public EmbeddingResponse() {
    }

    public EmbeddingResponse(String status, String embedding) {
        this.status = status;
        this.embedding = embedding;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmbedding() {
        return embedding;
    }

    public void setEmbedding(String embedding) {
        this.embedding = embedding;
    }
}
