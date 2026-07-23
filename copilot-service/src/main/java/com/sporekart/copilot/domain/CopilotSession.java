package com.sporekart.copilot.domain;

import com.sporekart.copilot.context.UserContext;
import com.sporekart.copilot.domain.CopilotStatus;
import com.sporekart.copilot.domain.CopilotType;
import com.sporekart.copilot.domain.SessionId;

import java.time.OffsetDateTime;

public record CopilotSession(
    SessionId id,
    CopilotType copilotType,
    UserContext user,
    CopilotStatus status,
    OffsetDateTime createdAt,
    OffsetDateTime lastActiveAt
) {}
