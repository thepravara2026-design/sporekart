package com.sporekart.copilot.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record CopilotInfoResponse(
    String id,
    String name,
    String type,
    String status,
    String version,
    List<String> capabilities,
    OffsetDateTime registeredAt
) {}
