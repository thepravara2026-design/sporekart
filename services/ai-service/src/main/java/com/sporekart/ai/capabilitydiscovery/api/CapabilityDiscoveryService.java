package com.sporekart.ai.capabilitydiscovery.api;

import com.sporekart.ai.capabilitydiscovery.domain.CapabilityAvailability;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityEntry;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityType;

import java.util.List;

public interface CapabilityDiscoveryService {

    List<CapabilityEntry> discoverByRequirement(List<String> requiredFeatures);

    List<CapabilityEntry> findCompatibleProviders(CapabilityType capabilityType);

    CapabilityAvailability checkAvailability(String capabilityId);
}
