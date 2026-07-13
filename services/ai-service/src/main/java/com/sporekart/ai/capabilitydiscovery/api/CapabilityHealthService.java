package com.sporekart.ai.capabilitydiscovery.api;

import com.sporekart.ai.capabilitydiscovery.domain.CapabilityAvailability;
import com.sporekart.ai.capabilitydiscovery.infrastructure.persistence.CapabilityHealthEntity;

import java.util.List;

public interface CapabilityHealthService {

    CapabilityHealthEntity recordAvailability(String capabilityId, CapabilityAvailability availability);

    CapabilityAvailability getCapabilityStatus(String capabilityId);

    List<CapabilityHealthEntity> getCapabilityHealthReport();
}
