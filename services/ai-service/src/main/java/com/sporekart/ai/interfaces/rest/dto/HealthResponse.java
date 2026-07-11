package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class HealthResponse {
    @Schema(description = "Status string", example = "ok")
    private String status;

    public HealthResponse() {
    }

    public HealthResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
