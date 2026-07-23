package com.sporekart.trainer.copilot.dto;

import java.util.List;
import java.util.Map;

import com.sporekart.trainer.copilot.domain.Lesson;

public record LessonResponse(
    List<Lesson> lessons,
    String totalDuration,
    String generationMethod,
    Map<String, Object> metadata
) {}
