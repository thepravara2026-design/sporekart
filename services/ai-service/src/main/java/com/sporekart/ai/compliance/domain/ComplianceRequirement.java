package com.sporekart.ai.compliance.domain;

import java.util.Map;

public record ComplianceRequirement(
    String requirementId,
    String framework,
    String clause,
    String description,
    boolean mandatory,
    Map<String, Object> controls
) {}
