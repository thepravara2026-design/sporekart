package com.sporekart.trainer.copilot.dto;

import java.util.List;

public record LessonRequest(
    String moduleId,
    String topic,
    String lessonType,
    String difficulty,
    int durationMinutes,
    List<String> learningObjectives
) {}
