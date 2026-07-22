package com.sporekart.ai.conversation.interfaces.rest.dto;

public class CitationResponse {
    private final String sourceId;
    private final String sourceType;
    private final String snippet;
    private final Integer relevanceScore;

    public CitationResponse(String sourceId, String sourceType, String snippet, Integer relevanceScore) {
        this.sourceId = sourceId;
        this.sourceType = sourceType;
        this.snippet = snippet;
        this.relevanceScore = relevanceScore;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getSnippet() {
        return snippet;
    }

    public Integer getRelevanceScore() {
        return relevanceScore;
    }
}
