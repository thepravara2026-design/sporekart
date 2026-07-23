package com.sporekart.grower.copilot.domain;

import java.util.List;
import java.util.Map;

public record GrowerProfile(
    String growerId,
    String farmLocation,
    String preferredMushroom,
    String experienceLevel,
    double farmArea,
    String climate,
    List<String> currentStages,
    Map<String, Object> preferences,
    List<String> completedTrainings
) {}
