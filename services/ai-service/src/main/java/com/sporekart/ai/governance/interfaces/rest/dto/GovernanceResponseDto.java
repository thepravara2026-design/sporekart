package com.sporekart.ai.governance.interfaces.rest.dto;

import java.util.List;
import java.util.Map;

public record GovernanceResponseDto(
    String requestId,
    String decision,
    List<GovernanceViolationDto> violations,
    Map<String, Object> context,
    long processingTimeMs
) {}
