package com.sporekart.ai.knowledgeregistry.interfaces.rest.dto;

public class KnowledgeSyncRequestDto {

    private boolean force;
    private String requestedBy;

    public KnowledgeSyncRequestDto() {
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }
}
