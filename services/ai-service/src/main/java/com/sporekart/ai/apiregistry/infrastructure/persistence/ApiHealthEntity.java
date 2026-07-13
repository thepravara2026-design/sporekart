package com.sporekart.ai.apiregistry.infrastructure.persistence;

import com.sporekart.ai.apiregistry.domain.ApiHealthStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ar_api_health",
        indexes = {
            @Index(name = "idx_ar_health_api_id", columnList = "api_id")
        })
public class ApiHealthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "api_id", nullable = false, length = 255)
    private String apiId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ApiHealthStatus status;

    @Column(name = "checked_at", nullable = false)
    private Instant checkedAt;

    public ApiHealthEntity() {
    }

    public ApiHealthEntity(UUID id, String apiId, ApiHealthStatus status, Instant checkedAt) {
        this.id = id;
        this.apiId = apiId;
        this.status = status;
        this.checkedAt = checkedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
