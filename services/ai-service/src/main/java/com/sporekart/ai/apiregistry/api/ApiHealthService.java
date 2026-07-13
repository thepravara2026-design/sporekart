package com.sporekart.ai.apiregistry.api;

import com.sporekart.ai.apiregistry.domain.ApiHealthStatus;

import java.util.List;
import java.util.Optional;

public interface ApiHealthService {

    void recordHealth(String apiId, ApiHealthStatus status);

    Optional<ApiHealthStatus> getHealth(String apiId);

    List<ApiHealthReport> getHealthReport();

    class ApiHealthReport {
        private String apiId;
        private ApiHealthStatus status;
        private java.time.Instant checkedAt;

        public ApiHealthReport() {
        }

        public ApiHealthReport(String apiId, ApiHealthStatus status, java.time.Instant checkedAt) {
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

        public java.time.Instant getCheckedAt() {
            return checkedAt;
        }

        public void setCheckedAt(java.time.Instant checkedAt) {
            this.checkedAt = checkedAt;
        }
    }
}
