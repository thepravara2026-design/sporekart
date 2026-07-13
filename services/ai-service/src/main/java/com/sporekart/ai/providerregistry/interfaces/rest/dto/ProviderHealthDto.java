package com.sporekart.ai.providerregistry.interfaces.rest.dto;

import com.sporekart.ai.providerregistry.domain.ProviderHealthStatus;

import java.time.Instant;

public class ProviderHealthDto {

    private String providerId;
    private ProviderHealthStatus status;
    private Instant checkedAt;

    public ProviderHealthDto() {}

    public ProviderHealthDto(String providerId, ProviderHealthStatus status, Instant checkedAt) {
        this.providerId = providerId;
        this.status = status;
        this.checkedAt = checkedAt;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public ProviderHealthStatus getStatus() {
        return status;
    }

    public void setStatus(ProviderHealthStatus status) {
        this.status = status;
    }

    public Instant getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(Instant checkedAt) {
        this.checkedAt = checkedAt;
    }
}
