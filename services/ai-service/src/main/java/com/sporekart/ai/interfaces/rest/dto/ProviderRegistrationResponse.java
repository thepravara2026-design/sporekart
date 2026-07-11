package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ProviderRegistrationResponse {
    @Schema(description = "Registration status", example = "registered")
    private String status;

    @Schema(description = "Provider name", example = "MOCK")
    private String provider;

    public ProviderRegistrationResponse() {
    }

    public ProviderRegistrationResponse(String status, String provider) {
        this.status = status;
        this.provider = provider;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
