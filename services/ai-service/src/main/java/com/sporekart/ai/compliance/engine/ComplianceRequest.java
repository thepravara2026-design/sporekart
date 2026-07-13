package com.sporekart.ai.compliance.engine;

import java.util.List;
import java.util.Map;

public record ComplianceRequest(
    String module,
    String action,
    Map<String, Object> context,
    String userId,
    List<String> roles
) {}
