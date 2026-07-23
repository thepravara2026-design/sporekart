package com.sporekart.trainer.copilot.dto;

public record AssessmentRequest(
    String moduleId,
    String title,
    String type,
    String difficulty,
    int numberOfQuestions,
    int durationMinutes
) {}
