package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class FeedbackResponse {
    @Schema(description = "Submission status", example = "accepted")
    private String status;

    @Schema(description = "Optional message")
    private String message;

    public FeedbackResponse() {
    }

    public FeedbackResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
