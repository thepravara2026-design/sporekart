package com.sporekart.ai.policy.interfaces.rest.dto;

import java.util.List;
import java.util.Map;

public record PolicyResponseDto(String id, String name, String description, String type, String status, String scope, int priority, String module, boolean active, Map<String, Object> metadata) {}
