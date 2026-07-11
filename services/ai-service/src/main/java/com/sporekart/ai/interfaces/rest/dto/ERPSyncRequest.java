package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ERPSyncRequest {
    @Schema(description = "ERP synchronization type", example = "INVENTORY")
    private String syncType;

    @Schema(description = "Configured ERP provider", example = "ERPNEXT")
    private String provider;

    public ERPSyncRequest() {
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
