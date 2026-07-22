package com.sporekart.ai.conversation.interfaces.rest.dto;

public class MemoryQueryRequest {
    private String query;
    private String layer;
    private String workspaceId;
    private String userId;
    private Integer limit;

    public MemoryQueryRequest() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
