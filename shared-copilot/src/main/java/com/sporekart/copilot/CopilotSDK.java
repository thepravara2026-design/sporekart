package com.sporekart.copilot;

import com.sporekart.copilot.capability.CapabilityRegistry;
import com.sporekart.copilot.capability.CapabilityRegistryImpl;
import com.sporekart.copilot.capability.DefaultCapabilities;
import com.sporekart.copilot.context.ContextAssembler;
import com.sporekart.copilot.context.ContextAssemblerImpl;
import com.sporekart.copilot.core.CopilotEngine;
import com.sporekart.copilot.core.CopilotEngineImpl;
import com.sporekart.copilot.event.EventBus;
import com.sporekart.copilot.event.EventBusImpl;
import com.sporekart.copilot.extension.CopilotExtensionContext;
import com.sporekart.copilot.extension.CopilotExtensionContextImpl;
import com.sporekart.copilot.memory.MemoryStore;
import com.sporekart.copilot.memory.MemoryStoreImpl;
import com.sporekart.copilot.permission.PermissionChecker;
import com.sporekart.copilot.permission.PermissionCheckerImpl;
import com.sporekart.copilot.persona.PersonaEngine;
import com.sporekart.copilot.persona.PersonaEngineImpl;
import com.sporekart.copilot.persona.PersonaRepository;
import com.sporekart.copilot.persona.PersonaRepositoryImpl;
import com.sporekart.copilot.plugin.CopilotPluginContext;
import com.sporekart.copilot.plugin.CopilotPluginContextImpl;
import com.sporekart.copilot.plugin.PluginManager;
import com.sporekart.copilot.plugin.PluginManagerImpl;
import com.sporekart.copilot.streaming.StreamingEngine;
import com.sporekart.copilot.streaming.StreamingEngineImpl;
import com.sporekart.copilot.tool.ToolExecutor;
import com.sporekart.copilot.tool.ToolExecutorImpl;
import com.sporekart.copilot.tool.ToolRegistry;
import com.sporekart.copilot.tool.ToolRegistryImpl;

import java.util.Objects;

public final class CopilotSDK {

    private CopilotSDK() {}

    public static CopilotEngine createDefaultEngine() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private ContextAssembler contextAssembler;
        private PersonaEngine personaEngine;
        private PermissionChecker permissionChecker;
        private CapabilityRegistry capabilityRegistry;
        private ToolExecutor toolExecutor;
        private MemoryStore memoryStore;
        private EventBus eventBus;
        private StreamingEngine streamingEngine;
        private ToolRegistry toolRegistry;
        private PersonaRepository personaRepository;
        private PluginManager pluginManager;

        private Builder() {}

        public Builder contextAssembler(ContextAssembler contextAssembler) {
            this.contextAssembler = contextAssembler;
            return this;
        }

        public Builder personaEngine(PersonaEngine personaEngine) {
            this.personaEngine = personaEngine;
            return this;
        }

        public Builder permissionChecker(PermissionChecker permissionChecker) {
            this.permissionChecker = permissionChecker;
            return this;
        }

        public Builder capabilityRegistry(CapabilityRegistry capabilityRegistry) {
            this.capabilityRegistry = capabilityRegistry;
            return this;
        }

        public Builder toolExecutor(ToolExecutor toolExecutor) {
            this.toolExecutor = toolExecutor;
            return this;
        }

        public Builder memoryStore(MemoryStore memoryStore) {
            this.memoryStore = memoryStore;
            return this;
        }

        public Builder eventBus(EventBus eventBus) {
            this.eventBus = eventBus;
            return this;
        }

        public Builder streamingEngine(StreamingEngine streamingEngine) {
            this.streamingEngine = streamingEngine;
            return this;
        }

        public Builder toolRegistry(ToolRegistry toolRegistry) {
            this.toolRegistry = toolRegistry;
            return this;
        }

        public Builder personaRepository(PersonaRepository personaRepository) {
            this.personaRepository = personaRepository;
            return this;
        }

        public CopilotEngine build() {
            MemoryStore memoryStore = resolveMemoryStore();
            EventBus eventBus = resolveEventBus();
            PersonaRepository personaRepository = resolvePersonaRepository();
            PersonaEngine personaEngine = resolvePersonaEngine(personaRepository);
            PermissionChecker permissionChecker = resolvePermissionChecker();
            CapabilityRegistry capabilityRegistry = resolveCapabilityRegistry();
            ToolRegistry toolRegistry = resolveToolRegistry();
            ToolExecutor toolExecutor = resolveToolExecutor(toolRegistry, permissionChecker);
            ContextAssembler contextAssembler = resolveContextAssembler(memoryStore);
            StreamingEngine streamingEngine = resolveStreamingEngine();

            CopilotExtensionContext extensionContext = new CopilotExtensionContextImpl(
                capabilityRegistry, toolRegistry, toolExecutor, eventBus,
                personaEngine, contextAssembler, memoryStore, streamingEngine, permissionChecker
            );

            CopilotPluginContext pluginContext = new CopilotPluginContextImpl(
                capabilityRegistry, toolRegistry, eventBus, personaEngine
            );

            PluginManager pluginManager = resolvePluginManager(pluginContext);

            return new CopilotEngineImpl(
                contextAssembler, personaEngine, permissionChecker, capabilityRegistry,
                toolExecutor, memoryStore, eventBus, streamingEngine
            );
        }

        private MemoryStore resolveMemoryStore() {
            return memoryStore != null ? memoryStore : new MemoryStoreImpl();
        }

        private EventBus resolveEventBus() {
            return eventBus != null ? eventBus : new EventBusImpl();
        }

        private PersonaRepository resolvePersonaRepository() {
            return personaRepository != null ? personaRepository : new PersonaRepositoryImpl();
        }

        private PersonaEngine resolvePersonaEngine(PersonaRepository repository) {
            return personaEngine != null ? personaEngine : new PersonaEngineImpl(repository);
        }

        private PermissionChecker resolvePermissionChecker() {
            return permissionChecker != null ? permissionChecker : new PermissionCheckerImpl();
        }

        private CapabilityRegistry resolveCapabilityRegistry() {
            if (capabilityRegistry != null) return capabilityRegistry;
            CapabilityRegistryImpl registry = new CapabilityRegistryImpl();
            DefaultCapabilities.all().forEach(registry::register);
            return registry;
        }

        private ToolRegistry resolveToolRegistry() {
            return toolRegistry != null ? toolRegistry : new ToolRegistryImpl();
        }

        private ToolExecutor resolveToolExecutor(ToolRegistry registry, PermissionChecker checker) {
            return toolExecutor != null ? toolExecutor : new ToolExecutorImpl(registry, checker);
        }

        private ContextAssembler resolveContextAssembler(MemoryStore store) {
            return contextAssembler != null ? contextAssembler : new ContextAssemblerImpl(store);
        }

        private StreamingEngine resolveStreamingEngine() {
            return streamingEngine != null ? streamingEngine : new StreamingEngineImpl();
        }

        private PluginManager resolvePluginManager(CopilotPluginContext pluginContext) {
            return pluginManager != null ? pluginManager : new PluginManagerImpl(pluginContext);
        }
    }
}
