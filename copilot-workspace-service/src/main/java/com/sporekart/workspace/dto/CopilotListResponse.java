package com.sporekart.workspace.dto;

import java.util.List;

public record CopilotListResponse(
    List<CopilotInfo> copilots,
    int total,
    int active,
    int enabled,
    int disabled
) {}
