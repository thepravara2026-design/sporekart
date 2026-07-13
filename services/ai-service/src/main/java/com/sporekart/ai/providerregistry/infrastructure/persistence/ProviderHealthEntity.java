package com.sporekart.ai.providerregistry.infrastructure.persistence;

import com.sporekart.ai.providerregistry.domain.ProviderHealthStatus;

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
@Table(name = "pr_provider_health",
        indexes = {
                @Index(name = "idx_pr_health_provider", columnList = "provider_id"),
                @Index(name = "idx_pr_health_checked", columnList = "checked_at")
        })
public class ProviderHealthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "provider_id", nullable = false, length = 64)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ProviderHealthStatus status;

    @Column(name = "checked_at", nullable = false)
    private Instant checkedAt;

    public ProviderHealthEntity() {}

    public ProviderHealthEntity(String providerId, ProviderHealthStatus status, Instant checkedAt) {
        this.providerId = providerId;
        this.status = status;
        this.checkedAt = checkedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
