package com.sporekart.ai.knowledge.interfaces.rest.dto;

import java.util.Map;

public class RagQueryRequest {
    private String query;
    private String workspaceId;
    private int topK = 10;
    private int maxContextTokens = 2000;
    private String strategy;
    private Map<String, String> filters;

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public String getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(String workspaceId) { this.workspaceId = workspaceId; }
    public int getTopK() { return topK; }
    public void setTopK(int topK) { this.topK = topK; }
    public int getMaxContextTokens() { return maxContextTokens; }
    public void setMaxContextTokens(int maxContextTokens) { this.maxContextTokens = maxContextTokens; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public Map<String, String> getFilters() { return filters; }
    public void setFilters(Map<String, String> filters) { this.filters = filters; }
}
