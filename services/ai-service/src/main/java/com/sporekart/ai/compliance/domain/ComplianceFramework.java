package com.sporekart.ai.compliance.domain;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ComplianceFramework(
    UUID id,
    String name,
    String version,
    ComplianceFrameworkType type,
    String description,
    String authority,
    List<ComplianceControl> controls,
    boolean active
) {}
