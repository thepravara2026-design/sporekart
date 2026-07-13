package com.sporekart.ai.policy.interfaces.rest.dto;

import java.util.Map;

public record PolicyHealthDto(String status, String service, long timestamp, Map<String, Object> details) {}
