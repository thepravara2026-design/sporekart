package com.sporekart.ai.knowledge.interfaces.rest.dto;

import java.util.List;

public class RagQueryResponse {
    private String query;
    private RagContextInfo context;
    private List<RagCitation> citations;
    private int totalResults;

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public RagContextInfo getContext() { return context; }
    public void setContext(RagContextInfo context) { this.context = context; }
    public List<RagCitation> getCitations() { return citations; }
    public void setCitations(List<RagCitation> citations) { this.citations = citations; }
    public int getTotalResults() { return totalResults; }
    public void setTotalResults(int totalResults) { this.totalResults = totalResults; }

    public static class RagContextInfo {
        private String assembledContext;
        private int totalTokens;
        private int maxTokens;
        private List<String> sources;

        public String getAssembledContext() { return assembledContext; }
        public void setAssembledContext(String assembledContext) { this.assembledContext = assembledContext; }
        public int getTotalTokens() { return totalTokens; }
        public void setTotalTokens(int totalTokens) { this.totalTokens = totalTokens; }
        public int getMaxTokens() { return maxTokens; }
        public void setMaxTokens(int maxTokens) { this.maxTokens = maxTokens; }
        public List<String> getSources() { return sources; }
        public void setSources(List<String> sources) { this.sources = sources; }
    }

    public static class RagCitation {
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
