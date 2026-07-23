package com.sporekart.grower.copilot.dto;

import java.util.List;

public record PlanningResponse(
    BusinessPlan plan,
    List<String> milestones,
    List<String> risks,
    String summary
) {}
