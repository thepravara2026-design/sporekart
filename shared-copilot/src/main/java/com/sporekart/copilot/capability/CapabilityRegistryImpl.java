package com.sporekart.copilot.capability;

import com.sporekart.copilot.domain.CopilotType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CapabilityRegistryImpl implements CapabilityRegistry {

    private static final Logger log = LoggerFactory.getLogger(CapabilityRegistryImpl.class);

    private final Map<String, Capability> capabilities = new ConcurrentHashMap<>();
    private final Map<String, Boolean> enabledFlags = new ConcurrentHashMap<>();

    public CapabilityRegistryImpl() {
        log.debug("CapabilityRegistryImpl initialized");
    }

    @Override
    public void register(Capability capability) {
        capabilities.put(capability.id(), capability);
        enabledFlags.put(capability.id(), true);
        log.info("Registered capability: {} ({})", capability.id(), capability.name());
    }

    @Override
    public Capability findById(String id) {
        return capabilities.get(id);
    }

    @Override
    public List<Capability> findByCopilotType(CopilotType type) {
        return capabilities.values().stream()
            .filter(cap -> cap.requiredTypes() == null || cap.requiredTypes().length == 0
                || Arrays.asList(cap.requiredTypes()).contains(type))
            .filter(cap -> isEnabled(cap.id()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Capability> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(capabilities.values()));
    }

    @Override
    public boolean isEnabled(String id) {
        return enabledFlags.getOrDefault(id, false);
    }

    @Override
    public void setEnabled(String id, boolean enabled) {
        if (!capabilities.containsKey(id)) {
            log.warn("Attempt to set enabled state for unknown capability: {}", id);
            return;
        }
        enabledFlags.put(id, enabled);
        log.info("Capability '{}' enabled set to {}", id, enabled);
    }
}
