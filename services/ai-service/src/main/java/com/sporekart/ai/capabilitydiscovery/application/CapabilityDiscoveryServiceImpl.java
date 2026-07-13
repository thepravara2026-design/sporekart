package com.sporekart.ai.capabilitydiscovery.application;

import com.sporekart.ai.capabilitydiscovery.api.CapabilityDiscoveryService;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityAvailability;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityEntry;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityType;
import com.sporekart.ai.capabilitydiscovery.infrastructure.persistence.CapabilityEntity;
import com.sporekart.ai.capabilitydiscovery.infrastructure.persistence.CapabilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CapabilityDiscoveryServiceImpl implements CapabilityDiscoveryService {

    private static final Logger log = LoggerFactory.getLogger(CapabilityDiscoveryServiceImpl.class);

    private final CapabilityRepository capabilityRepository;

    public CapabilityDiscoveryServiceImpl(CapabilityRepository capabilityRepository) {
        this.capabilityRepository = capabilityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CapabilityEntry> discoverByRequirement(List<String> requiredFeatures) {
        if (requiredFeatures == null || requiredFeatures.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> lower = requiredFeatures.stream().map(String::toLowerCase).collect(Collectors.toList());
        return capabilityRepository.findAll().stream()
                .filter(entity -> entity.getSupportedFeatures() != null
                        && lower.stream().allMatch(f -> entity.getSupportedFeatures().stream()
                        .map(String::toLowerCase).anyMatch(s -> s.contains(f))))
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CapabilityEntry> findCompatibleProviders(CapabilityType capabilityType) {
        return capabilityRepository.findByCapabilityType(capabilityType).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CapabilityAvailability checkAvailability(String capabilityId) {
        CapabilityEntity entity = capabilityRepository.findById(capabilityId)
                .orElseThrow(() -> new IllegalArgumentException("Capability not found: " + capabilityId));
        return entity.getAvailability();
    }

    private CapabilityEntry toDomain(CapabilityEntity entity) {
        CapabilityEntry entry = new CapabilityEntry();
        entry.setCapabilityId(entity.getCapabilityId());
        entry.setCapabilityName(entity.getCapabilityName());
        entry.setDescription(entity.getDescription());
        entry.setCapabilityType(entity.getCapabilityType());
        entry.setModule(entity.getModule());
        entry.setSupportedFeatures(entity.getSupportedFeatures() == null
                ? new ArrayList<>() : new ArrayList<>(entity.getSupportedFeatures()));
        entry.setDependencies(entity.getDependencies() == null
                ? new ArrayList<>() : new ArrayList<>(entity.getDependencies()));
        entry.setAvailability(entity.getAvailability());
        entry.setVersion(entity.getVersion());
        entry.setProviderCompatibility(entity.getProviderCompatibility() == null
                ? new ArrayList<>() : new ArrayList<>(entity.getProviderCompatibility()));
        entry.setMetadata(entity.getMetadata() == null
                ? new java.util.HashMap<>() : new java.util.HashMap<>(entity.getMetadata()));
        entry.setFeatureFlag(entity.getFeatureFlag());
        entry.setEnabled(entity.isEnabled());
        entry.setCreatedAt(entity.getCreatedAt());
        entry.setUpdatedAt(entity.getUpdatedAt());
        return entry;
    }
}
