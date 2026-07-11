package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

public class ERPStatusResponse {
    @Schema(description = "Arbitrary status details returned by ERP provider")
    private Map<String, Object> details;

    public ERPStatusResponse() {
    }

    public ERPStatusResponse(Map<String, Object> details) {
        this.details = details;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}
