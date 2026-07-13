package com.sporekart.ai.policy.interfaces.rest.dto;

import java.util.List;
import java.util.Map;

public record EvaluationRequestDto(String module, String action, Map<String, Object> payload, Map<String, Object> context, String userId, List<String> roles) {}
