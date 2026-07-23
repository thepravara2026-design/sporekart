package com.sporekart.copilot.core;

import com.sporekart.copilot.context.PageContext;
import com.sporekart.copilot.context.UserContext;
import com.sporekart.copilot.domain.CopilotResponse;
import com.sporekart.copilot.domain.CopilotStatus;
import com.sporekart.copilot.domain.CopilotType;
import com.sporekart.copilot.domain.SessionId;

import java.util.concurrent.CompletableFuture;

public interface CopilotEngine {

    CompletableFuture<CopilotResponse> processMessage(SessionId sessionId, String message, UserContext user, PageContext page);

    void startSession(SessionId sessionId, CopilotType type, UserContext user);

    void endSession(SessionId sessionId);

    CopilotStatus getStatus(SessionId sessionId);
}
