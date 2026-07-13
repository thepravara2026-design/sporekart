package com.sporekart.ai.decision.interfaces.rest.dto;
import java.util.Map;
public record ReasonDto(String code, String message, String category, String confidence, Map<String, Object> details) {}
