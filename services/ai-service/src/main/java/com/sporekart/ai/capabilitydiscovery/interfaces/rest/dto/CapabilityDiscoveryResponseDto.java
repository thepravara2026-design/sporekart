package com.sporekart.ai.capabilitydiscovery.interfaces.rest.dto;

import com.sporekart.ai.capabilitydiscovery.domain.CapabilityEntry;

import java.util.ArrayList;
import java.util.List;

public class CapabilityDiscoveryResponseDto {

    private List<CapabilityResponseDto> matchedCapabilities = new ArrayList<>();

    public CapabilityDiscoveryResponseDto() {
    }

    public static CapabilityDiscoveryResponseDto from(List<CapabilityEntry> entries) {
        CapabilityDiscoveryResponseDto dto = new CapabilityDiscoveryResponseDto();
        if (entries != null) {
            dto.setMatchedCapabilities(entries.stream()
                    .map(CapabilityResponseDto::from)
                    .collect(java.util.stream.Collectors.toList()));
        }
        return dto;
    }

    public List<CapabilityResponseDto> getMatchedCapabilities() {
        return matchedCapabilities;
    }

    public void setMatchedCapabilities(List<CapabilityResponseDto> matchedCapabilities) {
        this.matchedCapabilities = matchedCapabilities;
    }
}
