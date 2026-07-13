package com.sporekart.ai.policy.interfaces.rest.dto;

import java.util.Map;

public record PolicyRequestDto(String name, String description, String type, String scope, int priority, String module, Map<String, Object> rules, boolean active) {}
