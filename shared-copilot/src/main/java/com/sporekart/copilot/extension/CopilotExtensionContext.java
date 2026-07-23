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

public interface CopilotExtensionContext {

    CapabilityRegistry getCapabilityRegistry();

    ToolRegistry getToolRegistry();

    ToolExecutor getToolExecutor();

    EventBus getEventBus();

    PersonaEngine getPersonaEngine();

    ContextAssembler getContextAssembler();

    MemoryStore getMemoryStore();

    StreamingEngine getStreamingEngine();

    PermissionChecker getPermissionChecker();
}
