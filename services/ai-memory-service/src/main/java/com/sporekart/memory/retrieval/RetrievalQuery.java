package com.sporekart.memory.retrieval;

import java.util.List;

public record RetrievalQuery(
    String naturalLanguageQuery,
    List<String> tags,
    List<String> workspaces,
    List<String> memoryTypes,
    List<String> sources,
    int maxResults,
    double minRelevanceScore,
    boolean includeExpired,
    boolean useSemanticSearch,
    boolean useKeywordSearch,
    boolean useMetadataSearch,
    String ownerId,
    String ownerType,
    String entityType,
    String entityId,
    String conversationId,
    String department,
    Integer minImportance
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String naturalLanguageQuery;
        private List<String> tags;
        private List<String> workspaces;
        private List<String> memoryTypes;
        private List<String> sources;
        private int maxResults = 20;
        private double minRelevanceScore = 0.5;
        private boolean includeExpired;
        private boolean useSemanticSearch = true;
        private boolean useKeywordSearch = true;
        private boolean useMetadataSearch = true;
        private String ownerId;
        private String ownerType;
        private String entityType;
        private String entityId;
        private String conversationId;
        private String department;
        private Integer minImportance;

        public Builder naturalLanguageQuery(String v) { this.naturalLanguageQuery = v; return this; }
        public Builder tags(List<String> v) { this.tags = v; return this; }
        public Builder workspaces(List<String> v) { this.workspaces = v; return this; }
        public Builder memoryTypes(List<String> v) { this.memoryTypes = v; return this; }
        public Builder sources(List<String> v) { this.sources = v; return this; }
        public Builder maxResults(int v) { this.maxResults = v; return this; }
        public Builder minRelevanceScore(double v) { this.minRelevanceScore = v; return this; }
        public Builder includeExpired(boolean v) { this.includeExpired = v; return this; }
        public Builder useSemanticSearch(boolean v) { this.useSemanticSearch = v; return this; }
        public Builder useKeywordSearch(boolean v) { this.useKeywordSearch = v; return this; }
        public Builder useMetadataSearch(boolean v) { this.useMetadataSearch = v; return this; }
        public Builder ownerId(String v) { this.ownerId = v; return this; }
        public Builder ownerType(String v) { this.ownerType = v; return this; }
        public Builder entityType(String v) { this.entityType = v; return this; }
        public Builder entityId(String v) { this.entityId = v; return this; }
        public Builder conversationId(String v) { this.conversationId = v; return this; }
        public Builder department(String v) { this.department = v; return this; }
        public Builder minImportance(Integer v) { this.minImportance = v; return this; }

        public RetrievalQuery build() {
            return new RetrievalQuery(naturalLanguageQuery, tags, workspaces, memoryTypes, sources,
                    maxResults, minRelevanceScore, includeExpired, useSemanticSearch,
                    useKeywordSearch, useMetadataSearch, ownerId, ownerType, entityType,
                    entityId, conversationId, department, minImportance);
        }
    }
}
