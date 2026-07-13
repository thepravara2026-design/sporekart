package com.sporekart.ai.compliance.interfaces.rest.dto;

import java.util.List;
import java.util.Map;

public record ComplianceValidateRequest(
    String module,
    String action,
    Map<String, Object> context,
    String userId,
    List<String> roles
) {}
