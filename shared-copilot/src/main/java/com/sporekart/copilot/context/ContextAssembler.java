package com.sporekart.copilot.context;

import com.sporekart.copilot.domain.CopilotType;
import com.sporekart.copilot.domain.SessionId;

public interface ContextAssembler {

    CopilotContext assembleContext(UserContext user, PageContext page, CopilotType copilotType, SessionId sessionId);
}
