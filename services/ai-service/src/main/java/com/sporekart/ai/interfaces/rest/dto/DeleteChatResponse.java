package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class DeleteChatResponse {
    @Schema(description = "Operation status", example = "deleted")
    private String status;

    @Schema(description = "Session identifier that was deleted", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private String sessionId;

    public DeleteChatResponse() {
    }

    public DeleteChatResponse(String status, String sessionId) {
        this.status = status;
        this.sessionId = sessionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
