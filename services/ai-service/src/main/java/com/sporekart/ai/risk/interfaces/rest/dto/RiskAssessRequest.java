package com.sporekart.ai.risk.interfaces.rest.dto;

import java.util.List;
import java.util.Map;

public record RiskAssessRequest(
    String module,
    String action,
    Map<String, Object> context,
    String userId,
    List<String> roles
) {}
