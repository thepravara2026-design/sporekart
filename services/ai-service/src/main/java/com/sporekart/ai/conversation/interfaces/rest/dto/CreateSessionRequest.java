package com.sporekart.ai.conversation.interfaces.rest.dto;

public class CreateSessionRequest {
    private String userId;
    private String workspaceId;
    private Long idleTimeoutMinutes;

    public CreateSessionRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public Long getIdleTimeoutMinutes() {
        return idleTimeoutMinutes;
    }

    public void setIdleTimeoutMinutes(Long idleTimeoutMinutes) {
        this.idleTimeoutMinutes = idleTimeoutMinutes;
    }
}
