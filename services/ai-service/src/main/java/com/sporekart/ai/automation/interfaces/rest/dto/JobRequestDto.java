package com.sporekart.ai.automation.interfaces.rest.dto;

import java.util.Map;

public record JobRequestDto(
    String type,
    String name,
    Map<String, Object> params
) {}
