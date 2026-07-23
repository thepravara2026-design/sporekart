package com.sporekart.trainer.copilot.domain;

import java.util.List;

public record PracticalGuide(
    String guideId,
    String title,
    String category,
    String difficulty,
    String content,
    List<String> steps,
    List<String> requiredMaterials,
    List<String> safetyPrecautions,
    List<String> commonMistakes,
    List<String> references
) {}
