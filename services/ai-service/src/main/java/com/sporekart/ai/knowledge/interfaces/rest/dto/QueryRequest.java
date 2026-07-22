package com.sporekart.ai.knowledge.interfaces.rest.dto;

import java.util.Map;

public class QueryRequest {
    private String query;
    private int topK = 10;
    private String strategy = "HYBRID";
    private String workspaceId;
    private Map<String, String> filters;

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public int getTopK() { return topK; }
    public void setTopK(int topK) { this.topK = topK; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public String getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(String workspaceId) { this.workspaceId = workspaceId; }
    public Map<String, String> getFilters() { return filters; }
    public void setFilters(Map<String, String> filters) { this.filters = filters; }
}
