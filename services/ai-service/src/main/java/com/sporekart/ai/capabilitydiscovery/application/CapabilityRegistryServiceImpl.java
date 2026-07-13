package com.sporekart.ai.capabilitydiscovery.application;

import com.sporekart.ai.capabilitydiscovery.api.CapabilityRegistryService;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityAvailability;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityEntry;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityType;
import com.sporekart.ai.capabilitydiscovery.infrastructure.persistence.CapabilityEntity;
import com.sporekart.ai.capabilitydiscovery.infrastructure.persistence.CapabilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CapabilityRegistryServiceImpl implements CapabilityRegistryService {

    private static final Logger log = LoggerFactory.getLogger(CapabilityRegistryServiceImpl.class);

    private final CapabilityRepository capabilityRepository;

    public CapabilityRegistryServiceImpl(CapabilityRepository capabilityRepository) {
        this.capabilityRepository = capabilityRepository;
    }

    @Override
    @Transactional
    public CapabilityEntry registerCapability(CapabilityEntry entry) {
        CapabilityEntity entity = new CapabilityEntity();
        String capabilityId = entry.getCapabilityId();
        if (capabilityId == null || capabilityId.isBlank()) {
            capabilityId = UUID.randomUUID().toString();
        }
        entity.setCapabilityId(capabilityId);
        applyToEntity(entry, entity);
        Instant now = Instant.now();
        entity.setEnabled(entry.isEnabled());
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        if (entity.getAvailability() == null) {
            entity.setAvailability(CapabilityAvailability.AVAILABLE);
        }
        CapabilityEntity saved = capabilityRepository.save(entity);
        log.info("Registered capability: {}", saved.getCapabilityId());
        return toDomain(saved);
    }

    @Override
    @Transactional
    public CapabilityEntry updateCapability(String capabilityId, CapabilityEntry entry) {
        CapabilityEntity entity = capabilityRepository.findById(capabilityId)
                .orElseThrow(() -> new IllegalArgumentException("Capability not found: " + capabilityId));
        applyToEntity(entry, entity);
        entity.setUpdatedAt(Instant.now());
        CapabilityEntity saved = capabilityRepository.save(entity);
        log.info("Updated capability: {}", saved.getCapabilityId());
        return toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CapabilityEntry getCapability(String capabilityId) {
        return capabilityRepository.findById(capabilityId)
                .map(this::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Capability not found: " + capabilityId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CapabilityEntry> listCapabilities() {
        return capabilityRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CapabilityEntry> searchByType(CapabilityType capabilityType) {
        return capabilityRepository.findByCapabilityType(capabilityType).stream()
                .map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CapabilityEntry> searchByModule(String module) {
        return capabilityRepository.findByModule(module).stream()
                .map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CapabilityEntry> searchByFeature(String feature) {
        return capabilityRepository.findBySupportedFeaturesContaining(feature).stream()
                .map(this::toDomain).collect(Collectors.toList());
    }

    private void applyToEntity(CapabilityEntry entry, CapabilityEntity entity) {
        entity.setCapabilityName(entry.getCapabilityName());
        entity.setCapabilityType(entry.getCapabilityType());
        entity.setDescription(entry.getDescription());
        entity.setModule(entry.getModule());
        entity.setSupportedFeatures(entry.getSupportedFeatures() == null
                ? new ArrayList<>() : new ArrayList<>(entry.getSupportedFeatures()));
        entity.setDependencies(entry.getDependencies() == null
                ? new ArrayList<>() : new ArrayList<>(entry.getDependencies()));
        if (entry.getAvailability() != null) {
            entity.setAvailability(entry.getAvailability());
        }
        entity.setVersion(entry.getVersion());
        entity.setProviderCompatibility(entry.getProviderCompatibility() == null
                ? new ArrayList<>() : new ArrayList<>(entry.getProviderCompatibility()));
        entity.setMetadata(entry.getMetadata() == null
                ? new HashMap<>() : new HashMap<>(entry.getMetadata()));
        entity.setFeatureFlag(entry.getFeatureFlag());
        entity.setEnabled(entry.isEnabled());
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
                ? new HashMap<>() : new HashMap<>(entity.getMetadata()));
        entry.setFeatureFlag(entity.getFeatureFlag());
        entry.setEnabled(entity.isEnabled());
        entry.setCreatedAt(entity.getCreatedAt());
        entry.setUpdatedAt(entity.getUpdatedAt());
        return entry;
    }
}
