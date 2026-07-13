package com.sporekart.ai.compliance.interfaces.rest.dto;

import java.util.List;

public record ComplianceValidateResponse(
    String id,
    boolean compliant,
    String status,
    List<String> violations,
    List<String> findings,
    String message,
    long timestamp
) {}
