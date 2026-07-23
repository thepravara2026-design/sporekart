package com.sporekart.copilot.extension;

import com.sporekart.copilot.capability.CapabilityRegistry;
import com.sporekart.copilot.context.ContextAssembler;
import com.sporekart.copilot.event.EventBus;
import com.sporekart.copilot.memory.MemoryStore;
import com.sporekart.copilot.permission.PermissionChecker;
import com.sporekart.copilot.persona.PersonaEngine;
import com.sporekart.copilot.streaming.StreamingEngine;
import com.sporekart.copilot.tool.ToolExecutor;
import com.sporekart.copilot.tool.ToolRegistry;

import java.util.Objects;

public class CopilotExtensionContextImpl implements CopilotExtensionContext {

    private final CapabilityRegistry capabilityRegistry;
    private final ToolRegistry toolRegistry;
    private final ToolExecutor toolExecutor;
    private final EventBus eventBus;
    private final PersonaEngine personaEngine;
    private final ContextAssembler contextAssembler;
    private final MemoryStore memoryStore;
    private final StreamingEngine streamingEngine;
    private final PermissionChecker permissionChecker;

    public CopilotExtensionContextImpl(
        CapabilityRegistry capabilityRegistry,
        ToolRegistry toolRegistry,
        ToolExecutor toolExecutor,
        EventBus eventBus,
        PersonaEngine personaEngine,
        ContextAssembler contextAssembler,
        MemoryStore memoryStore,
        StreamingEngine streamingEngine,
        PermissionChecker permissionChecker
    ) {
        this.capabilityRegistry = Objects.requireNonNull(capabilityRegistry);
        this.toolRegistry = Objects.requireNonNull(toolRegistry);
        this.toolExecutor = Objects.requireNonNull(toolExecutor);
        this.eventBus = Objects.requireNonNull(eventBus);
        this.personaEngine = Objects.requireNonNull(personaEngine);
        this.contextAssembler = Objects.requireNonNull(contextAssembler);
        this.memoryStore = Objects.requireNonNull(memoryStore);
        this.streamingEngine = Objects.requireNonNull(streamingEngine);
        this.permissionChecker = Objects.requireNonNull(permissionChecker);
    }

    @Override
    public CapabilityRegistry getCapabilityRegistry() { return capabilityRegistry; }

    @Override
    public ToolRegistry getToolRegistry() { return toolRegistry; }

    @Override
    public ToolExecutor getToolExecutor() { return toolExecutor; }

    @Override
    public EventBus getEventBus() { return eventBus; }

    @Override
    public PersonaEngine getPersonaEngine() { return personaEngine; }

    @Override
    public ContextAssembler getContextAssembler() { return contextAssembler; }

    @Override
    public MemoryStore getMemoryStore() { return memoryStore; }

    @Override
    public StreamingEngine getStreamingEngine() { return streamingEngine; }

    @Override
    public PermissionChecker getPermissionChecker() { return permissionChecker; }
}
