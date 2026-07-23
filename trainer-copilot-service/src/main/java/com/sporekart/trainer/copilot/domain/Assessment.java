package com.sporekart.trainer.copilot.domain;

import java.util.List;

public record Question(
    String questionId,
    String questionText,
    String questionType,
    List<String> options,
    String correctAnswer,
    int marks,
    String explanation
) {}

public record Assessment(
    String assessmentId,
    String moduleId,
    String title,
    String type,
    String difficulty,
    List<Question> questions,
    int totalMarks,
    int passingMarks,
    int durationMinutes,
    String answerKey
) {}
