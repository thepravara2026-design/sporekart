package com.sporekart.trainer.copilot.dto;

import com.sporekart.trainer.copilot.domain.Assessment;

public record AssessmentResponse(
    Assessment assessment,
    String answerKey
) {}
