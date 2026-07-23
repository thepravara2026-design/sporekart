package com.sporekart.grower.copilot.dto;

public record PlanningRequest(
    String mushroomType,
    double farmArea,
    double budget,
    String experienceLevel,
    String location
) {}
