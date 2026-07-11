package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ERPConfigureRequest {
    @Schema(description = "ERP provider identifier", example = "ERPNEXT")
    private String provider;

    @Schema(description = "Provider endpoint URI", example = "https://erpnext.example.com")
    private String endpoint;

    @Schema(description = "Provider API key", example = "placeholder-secret-key")
    private String apiKey;

    public ERPConfigureRequest() {
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
