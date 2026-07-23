package com.sporekart.trainer.copilot.domain;

import java.util.List;

public record Lesson(
    String lessonId,
    String moduleId,
    String title,
    String content,
    String lessonType,
    int durationMinutes,
    List<String> learningObjectives,
    List<String> materials,
    List<String> activities,
    List<String> assessmentRefs,
    String difficulty
) {}
