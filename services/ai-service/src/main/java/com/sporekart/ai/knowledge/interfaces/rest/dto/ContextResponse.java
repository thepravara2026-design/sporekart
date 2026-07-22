package com.sporekart.ai.knowledge.interfaces.rest.dto;

import java.util.List;

public class ContextResponse {
    private String query;
    private String context;
    private int totalTokens;
    private int maxTokens;
    private List<String> sources;
    private List<CitationInfo> citations;

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }
    public int getTotalTokens() { return totalTokens; }
    public void setTotalTokens(int totalTokens) { this.totalTokens = totalTokens; }
    public int getMaxTokens() { return maxTokens; }
    public void setMaxTokens(int maxTokens) { this.maxTokens = maxTokens; }
    public List<String> getSources() { return sources; }
    public void setSources(List<String> sources) { this.sources = sources; }
    public List<CitationInfo> getCitations() { return citations; }
    public void setCitations(List<CitationInfo> citations) { this.citations = citations; }

    public static class CitationInfo {
        private String citationId;
        private String documentId;
        private String documentTitle;
        private String source;
        private String excerpt;
        private double relevanceScore;

        public String getCitationId() { return citationId; }
        public void setCitationId(String citationId) { this.citationId = citationId; }
        public String getDocumentId() { return documentId; }
        public void setDocumentId(String documentId) { this.documentId = documentId; }
        public String getDocumentTitle() { return documentTitle; }
        public void setDocumentTitle(String documentTitle) { this.documentTitle = documentTitle; }
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        public String getExcerpt() { return excerpt; }
        public void setExcerpt(String excerpt) { this.excerpt = excerpt; }
        public double getRelevanceScore() { return relevanceScore; }
        public void setRelevanceScore(double relevanceScore) { this.relevanceScore = relevanceScore; }
    }
}
