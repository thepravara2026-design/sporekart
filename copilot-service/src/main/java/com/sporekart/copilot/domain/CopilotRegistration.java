package com.sporekart.copilot.domain;

import com.sporekart.copilot.domain.CopilotStatus;
import com.sporekart.copilot.domain.CopilotType;
import com.sporekart.copilot.persona.Persona;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record CopilotRegistration(
    String id,
    String name,
    CopilotType type,
    String version,
    String description,
    Persona persona,
    List<String> capabilityIds,
    CopilotStatus status,
    Map<String, String> metadata,
    OffsetDateTime registeredAt
) {}
