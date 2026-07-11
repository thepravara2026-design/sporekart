package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class SyncCompleteResponse {
    @Schema(description = "Indicates whether the operation was successful", example = "true")
    private boolean success;

    @Schema(description = "Human-readable response message", example = "Sync completed")
    private String message;

    public SyncCompleteResponse() {
    }

    public SyncCompleteResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
