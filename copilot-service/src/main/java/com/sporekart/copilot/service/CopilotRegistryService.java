package com.sporekart.copilot.service;

import com.sporekart.copilot.capability.Capability;
import com.sporekart.copilot.capability.CapabilityRegistry;
import com.sporekart.copilot.domain.CopilotStatus;
import com.sporekart.copilot.domain.CopilotType;
import com.sporekart.copilot.dto.RegisterCopilotRequest;
import com.sporekart.copilot.persona.DefaultPersonas;
import com.sporekart.copilot.persona.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CopilotRegistryService {

    private static final Logger log = LoggerFactory.getLogger(CopilotRegistryService.class);

    private final Map<String, com.sporekart.copilot.domain.CopilotRegistration> registry = new ConcurrentHashMap<>();
    private final CapabilityRegistry capabilityRegistry;

    public CopilotRegistryService(CapabilityRegistry capabilityRegistry) {
        this.capabilityRegistry = capabilityRegistry;
    }

    public com.sporekart.copilot.domain.CopilotRegistration registerCopilot(RegisterCopilotRequest request) {
        String id = UUID.randomUUID().toString();
        CopilotType type;
        try {
            type = CopilotType.valueOf(request.type().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid copilot type: " + request.type(), e);
        }

        Persona persona = resolvePersona(request.persona());
        List<String> capabilityIds = request.capabilities() != null ? request.capabilities() : List.of();

        for (String capId : capabilityIds) {
            if (capabilityRegistry.findById(capId) == null) {
                log.warn("Capability {} not found in registry, registering placeholder", capId);
            }
        }

        var registration = new com.sporekart.copilot.domain.CopilotRegistration(
            id, request.name(), type, request.version(),
            request.description() != null ? request.description() : "",
            persona, capabilityIds, CopilotStatus.ACTIVE,
            new HashMap<>(), OffsetDateTime.now()
        );
        registry.put(id, registration);
        log.info("Registered copilot: {} ({}) with id {}", request.name(), type, id);
        return registration;
    }

    public void enableCopilot(String id) {
        var existing = getExisting(id);
        var updated = new com.sporekart.copilot.domain.CopilotRegistration(
            existing.id(), existing.name(), existing.type(), existing.version(),
            existing.description(), existing.persona(), existing.capabilityIds(),
            CopilotStatus.ACTIVE, existing.metadata(), existing.registeredAt()
        );
        registry.put(id, updated);
        log.info("Enabled copilot: {}", id);
    }

    public void disableCopilot(String id) {
        var existing = getExisting(id);
        var updated = new com.sporekart.copilot.domain.CopilotRegistration(
            existing.id(), existing.name(), existing.type(), existing.version(),
            existing.description(), existing.persona(), existing.capabilityIds(),
            CopilotStatus.INACTIVE, existing.metadata(), existing.registeredAt()
        );
        registry.put(id, updated);
        log.info("Disabled copilot: {}", id);
    }

    public com.sporekart.copilot.domain.CopilotRegistration getCopilot(String id) {
        return getExisting(id);
    }

    public List<com.sporekart.copilot.domain.CopilotRegistration> listCopilots() {
        return List.copyOf(registry.values());
    }

    public com.sporekart.copilot.domain.CopilotRegistration getCopilotHealth(String id) {
        return getExisting(id);
    }

    public List<Capability> getCapabilities() {
        return capabilityRegistry.findAll();
    }

    public List<Persona> getPersonas() {
        return DefaultPersonas.all();
    }

    private com.sporekart.copilot.domain.CopilotRegistration getExisting(String id) {
        var registration = registry.get(id);
        if (registration == null) {
            throw new NoSuchElementException("Copilot not found: " + id);
        }
        return registration;
    }

    private Persona resolvePersona(Map<String, Object> personaMap) {
        if (personaMap == null || personaMap.isEmpty()) {
            return DefaultPersonas.customerPersona();
        }
        return DefaultPersonas.customerPersona();
    }
}
