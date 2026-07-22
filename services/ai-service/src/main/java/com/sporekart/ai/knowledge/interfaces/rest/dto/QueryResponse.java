package com.sporekart.ai.knowledge.interfaces.rest.dto;

import java.util.List;

public class QueryResponse {
    private String query;
    private List<SearchResultItem> results;
    private int totalResults;
    private String strategy;

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public List<SearchResultItem> getResults() { return results; }
    public void setResults(List<SearchResultItem> results) { this.results = results; }
    public int getTotalResults() { return totalResults; }
    public void setTotalResults(int totalResults) { this.totalResults = totalResults; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }

    public static class SearchResultItem {
        private String chunkId;
        private String documentId;
        private String documentTitle;
        private String content;
        private double score;
        private int chunkIndex;
        private String source;
        private String section;
        private String citationId;
        private String citationSource;
        private double citationConfidence;
        private String citationExcerpt;

        public String getChunkId() { return chunkId; }
        public void setChunkId(String chunkId) { this.chunkId = chunkId; }
        public String getDocumentId() { return documentId; }
        public void setDocumentId(String documentId) { this.documentId = documentId; }
        public String getDocumentTitle() { return documentTitle; }
        public void setDocumentTitle(String documentTitle) { this.documentTitle = documentTitle; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public double getScore() { return score; }
        public void setScore(double score) { this.score = score; }
        public int getChunkIndex() { return chunkIndex; }
        public void setChunkIndex(int chunkIndex) { this.chunkIndex = chunkIndex; }
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        public String getSection() { return section; }
        public void setSection(String section) { this.section = section; }
        public String getCitationId() { return citationId; }
        public void setCitationId(String citationId) { this.citationId = citationId; }
        public String getCitationSource() { return citationSource; }
        public void setCitationSource(String citationSource) { this.citationSource = citationSource; }
        public double getCitationConfidence() { return citationConfidence; }
        public void setCitationConfidence(double citationConfidence) { this.citationConfidence = citationConfidence; }
        public String getCitationExcerpt() { return citationExcerpt; }
        public void setCitationExcerpt(String citationExcerpt) { this.citationExcerpt = citationExcerpt; }
    }
}
