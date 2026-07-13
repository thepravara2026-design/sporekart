package com.sporekart.ai.capabilitydiscovery.application;

import com.sporekart.ai.capabilitydiscovery.api.CapabilityHealthService;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityAvailability;
import com.sporekart.ai.capabilitydiscovery.infrastructure.persistence.CapabilityEntity;
import com.sporekart.ai.capabilitydiscovery.infrastructure.persistence.CapabilityHealthEntity;
import com.sporekart.ai.capabilitydiscovery.infrastructure.persistence.CapabilityHealthRepository;
import com.sporekart.ai.capabilitydiscovery.infrastructure.persistence.CapabilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class CapabilityHealthServiceImpl implements CapabilityHealthService {

    private static final Logger log = LoggerFactory.getLogger(CapabilityHealthServiceImpl.class);

    private final CapabilityHealthRepository healthRepository;
    private final CapabilityRepository capabilityRepository;

    public CapabilityHealthServiceImpl(CapabilityHealthRepository healthRepository,
                                       CapabilityRepository capabilityRepository) {
        this.healthRepository = healthRepository;
        this.capabilityRepository = capabilityRepository;
    }

    @Override
    @Transactional
    public CapabilityHealthEntity recordAvailability(String capabilityId, CapabilityAvailability availability) {
        CapabilityEntity entity = capabilityRepository.findById(capabilityId)
                .orElseThrow(() -> new IllegalArgumentException("Capability not found: " + capabilityId));
        Instant now = Instant.now();
        CapabilityHealthEntity health = new CapabilityHealthEntity(capabilityId, availability, now);
        CapabilityHealthEntity saved = healthRepository.save(health);
        entity.setAvailability(availability);
        entity.setUpdatedAt(now);
        capabilityRepository.save(entity);
        log.info("Recorded availability {} for capability: {}", availability, capabilityId);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public CapabilityAvailability getCapabilityStatus(String capabilityId) {
        CapabilityEntity entity = capabilityRepository.findById(capabilityId)
                .orElseThrow(() -> new IllegalArgumentException("Capability not found: " + capabilityId));
        return entity.getAvailability();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CapabilityHealthEntity> getCapabilityHealthReport() {
        return healthRepository.findAll().stream()
                .sorted((a, b) -> b.getCheckedAt().compareTo(a.getCheckedAt()))
                .collect(java.util.stream.Collectors.toList());
    }
}
