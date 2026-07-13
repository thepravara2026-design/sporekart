package com.sporekart.ai.capabilitydiscovery.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sporekart.ai.capabilitydiscovery.domain.CapabilityAvailability;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityEntry;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityType;
import com.sporekart.ai.capabilitydiscovery.infrastructure.persistence.CapabilityEntity;
import com.sporekart.ai.capabilitydiscovery.infrastructure.persistence.CapabilityRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CapabilityDiscoveryServiceTest {

    @Mock
    private CapabilityRepository capabilityRepository;

    @InjectMocks
    private CapabilityRegistryServiceImpl registryService;

    @InjectMocks
    private CapabilityDiscoveryServiceImpl discoveryService;

    @Test
    void registerCapabilityPersistsEntry() {
        CapabilityEntry entry = new CapabilityEntry();
        entry.setCapabilityName("Vision");
        entry.setCapabilityType(CapabilityType.AI_MODEL);
        entry.setModule("vision");

        when(capabilityRepository.save(any(CapabilityEntity.class))).thenAnswer(i -> i.getArgument(0));

        CapabilityEntry saved = registryService.registerCapability(entry);

        assertNotNull(saved);
        assertEquals("Vision", saved.getCapabilityName());
    }

    @Test
    void discoverByRequirementFiltersCapabilities() {
        CapabilityEntity vision = new CapabilityEntity();
        vision.setCapabilityId("c1");
        vision.setCapabilityName("Vision");
        vision.setSupportedFeatures(List.of("vision", "ocr"));

        CapabilityEntity speech = new CapabilityEntity();
        speech.setCapabilityId("c2");
        speech.setCapabilityName("Speech");
        speech.setSupportedFeatures(List.of("speech"));

        when(capabilityRepository.findAll()).thenReturn(List.of(vision, speech));

        List<CapabilityEntry> matched = discoveryService.discoverByRequirement(List.of("vision"));

        assertEquals(1, matched.size());
        assertEquals("Vision", matched.get(0).getCapabilityName());
    }

    @Test
    void checkAvailabilityReturnsStatus() {
        CapabilityEntity entity = new CapabilityEntity();
        entity.setCapabilityId("c1");
        entity.setAvailability(CapabilityAvailability.AVAILABLE);
        when(capabilityRepository.findById("c1")).thenReturn(Optional.of(entity));

        CapabilityAvailability availability = discoveryService.checkAvailability("c1");

        assertEquals(CapabilityAvailability.AVAILABLE, availability);
    }
}
