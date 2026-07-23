package com.sporekart.copilot.plugin;

import com.sporekart.copilot.capability.CapabilityRegistry;
import com.sporekart.copilot.event.EventBus;
import com.sporekart.copilot.persona.PersonaEngine;
import com.sporekart.copilot.tool.ToolRegistry;

public interface CopilotPluginContext {

    CapabilityRegistry getCapabilityRegistry();

    ToolRegistry getToolRegistry();

    EventBus getEventBus();

    PersonaEngine getPersonaEngine();
}
