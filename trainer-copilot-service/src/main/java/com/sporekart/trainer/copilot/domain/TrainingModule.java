package com.sporekart.trainer.copilot.domain;

import java.util.List;

public record TrainingModule(
    String moduleId,
    String moduleName,
    String description,
    int durationHours,
    String moduleType,
    List<String> learningObjectives,
    List<String> prerequisites,
    List<String> resources
) {}
