package com.sporekart.ai.compliance.interfaces.rest.dto;

public record RuleDto(
    String id,
    String frameworkId,
    String ruleId,
    String name,
    String description,
    String category,
    String riskLevel,
    boolean active
) {}
