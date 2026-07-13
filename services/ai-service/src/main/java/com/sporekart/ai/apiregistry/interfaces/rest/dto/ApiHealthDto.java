package com.sporekart.ai.apiregistry.interfaces.rest.dto;

import com.sporekart.ai.apiregistry.domain.ApiHealthStatus;

import java.time.Instant;

public class ApiHealthDto {

    private String apiId;
    private ApiHealthStatus status;
    private Instant checkedAt;

    public ApiHealthDto() {
    }

    public ApiHealthDto(String apiId, ApiHealthStatus status, Instant checkedAt) {
        this.apiId = apiId;
        this.status = status;
        this.checkedAt = checkedAt;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public ApiHealthStatus getStatus() {
        return status;
    }

    public void setStatus(ApiHealthStatus status) {
        this.status = status;
    }

    public Instant getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(Instant checkedAt) {
        this.checkedAt = checkedAt;
    }
}
