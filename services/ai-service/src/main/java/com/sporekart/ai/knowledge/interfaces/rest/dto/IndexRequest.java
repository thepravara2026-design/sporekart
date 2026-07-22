package com.sporekart.ai.knowledge.interfaces.rest.dto;

public class IndexRequest {
    private String documentId;
    private String embeddingProvider;
    private String vectorStoreProvider;

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
    public String getEmbeddingProvider() { return embeddingProvider; }
    public void setEmbeddingProvider(String embeddingProvider) { this.embeddingProvider = embeddingProvider; }
    public String getVectorStoreProvider() { return vectorStoreProvider; }
    public void setVectorStoreProvider(String vectorStoreProvider) { this.vectorStoreProvider = vectorStoreProvider; }
}
