package com.sporekart.copilot.persona;

import com.sporekart.copilot.context.UserContext;
import com.sporekart.copilot.domain.CopilotType;

public interface PersonaEngine {

    Persona resolvePersona(CopilotType copilotType, UserContext userContext);

    Persona getDefaultPersona(CopilotType copilotType);
}
