package com.sporekart.ai.automation.interfaces.rest.dto;

import java.util.Map;

public record ScheduleCreateDto(
    String name,
    String jobType,
    String frequency,
    String cronExpression,
    Map<String, Object> params
) {}
