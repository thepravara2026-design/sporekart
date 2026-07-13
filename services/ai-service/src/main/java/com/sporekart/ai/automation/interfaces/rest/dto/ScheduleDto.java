package com.sporekart.ai.automation.interfaces.rest.dto;

public record ScheduleDto(
    String id,
    String name,
    String jobType,
    String frequency,
    String cronExpression,
    boolean active,
    String nextRunAt
) {}
