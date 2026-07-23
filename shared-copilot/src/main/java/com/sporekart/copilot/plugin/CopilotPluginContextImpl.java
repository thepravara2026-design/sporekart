package com.sporekart.copilot.plugin;

import com.sporekart.copilot.capability.CapabilityRegistry;
import com.sporekart.copilot.event.EventBus;
import com.sporekart.copilot.persona.PersonaEngine;
import com.sporekart.copilot.tool.ToolRegistry;

import java.util.Objects;

public class CopilotPluginContextImpl implements CopilotPluginContext {

    private final CapabilityRegistry capabilityRegistry;
    private final ToolRegistry toolRegistry;
    private final EventBus eventBus;
    private final PersonaEngine personaEngine;

    public CopilotPluginContextImpl(
        CapabilityRegistry capabilityRegistry,
        ToolRegistry toolRegistry,
        EventBus eventBus,
        PersonaEngine personaEngine
    ) {
        this.capabilityRegistry = Objects.requireNonNull(capabilityRegistry, "capabilityRegistry must not be null");
        this.toolRegistry = Objects.requireNonNull(toolRegistry, "toolRegistry must not be null");
        this.eventBus = Objects.requireNonNull(eventBus, "eventBus must not be null");
        this.personaEngine = Objects.requireNonNull(personaEngine, "personaEngine must not be null");
    }

    @Override
    public CapabilityRegistry getCapabilityRegistry() {
        return capabilityRegistry;
    }

    @Override
    public ToolRegistry getToolRegistry() {
        return toolRegistry;
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public PersonaEngine getPersonaEngine() {
        return personaEngine;
    }
}
