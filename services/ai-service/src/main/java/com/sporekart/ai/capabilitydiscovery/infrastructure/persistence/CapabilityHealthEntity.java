package com.sporekart.ai.capabilitydiscovery.infrastructure.persistence;

import com.sporekart.ai.capabilitydiscovery.domain.CapabilityAvailability;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "cd_capability_health")
public class CapabilityHealthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "capability_id", nullable = false, length = 255)
    private String capabilityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability", nullable = false, length = 32)
    private CapabilityAvailability availability;

    @Column(name = "checked_at", nullable = false)
    private Instant checkedAt;

    public CapabilityHealthEntity() {
    }

    public CapabilityHealthEntity(String capabilityId, CapabilityAvailability availability, Instant checkedAt) {
        this.capabilityId = capabilityId;
        this.availability = availability;
        this.checkedAt = checkedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
    }

    public CapabilityAvailability getAvailability() {
        return availability;
    }

    public void setAvailability(CapabilityAvailability availability) {
        this.availability = availability;
    }

    public Instant getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(Instant checkedAt) {
        this.checkedAt = checkedAt;
    }
}
