package com.sporekart.ai.capabilitydiscovery.interfaces.rest.dto;

import com.sporekart.ai.capabilitydiscovery.domain.CapabilityAvailability;
import com.sporekart.ai.capabilitydiscovery.infrastructure.persistence.CapabilityHealthEntity;

import java.time.Instant;

public class CapabilityHealthDto {

    private Long id;
    private String capabilityId;
    private CapabilityAvailability availability;
    private Instant checkedAt;

    public CapabilityHealthDto() {
    }

    public static CapabilityHealthDto from(CapabilityHealthEntity entity) {
        CapabilityHealthDto dto = new CapabilityHealthDto();
        dto.setId(entity.getId());
        dto.setCapabilityId(entity.getCapabilityId());
        dto.setAvailability(entity.getAvailability());
        dto.setCheckedAt(entity.getCheckedAt());
        return dto;
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
