package com.sporekart.trainer.copilot.domain;

import java.util.List;

public record CultivationStep(
    String stepId,
    String stepName,
    int stepOrder,
    String description,
    int durationDays,
    String temperatureRange,
    String humidityRange,
    String lightRequirement,
    List<String> checkpoints,
    List<String> warnings
) {}
