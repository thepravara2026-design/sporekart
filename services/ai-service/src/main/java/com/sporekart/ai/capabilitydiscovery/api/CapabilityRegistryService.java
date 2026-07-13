package com.sporekart.ai.capabilitydiscovery.api;

import com.sporekart.ai.capabilitydiscovery.domain.CapabilityEntry;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityType;

import java.util.List;

public interface CapabilityRegistryService {

    CapabilityEntry registerCapability(CapabilityEntry entry);

    CapabilityEntry updateCapability(String capabilityId, CapabilityEntry entry);

    CapabilityEntry getCapability(String capabilityId);

    List<CapabilityEntry> listCapabilities();

    List<CapabilityEntry> searchByType(CapabilityType capabilityType);

    List<CapabilityEntry> searchByModule(String module);

    List<CapabilityEntry> searchByFeature(String feature);
}
