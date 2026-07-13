package com.sporekart.ai.policy.interfaces.rest.dto;

import java.util.List;

public record EvaluationResultDto(String requestId, String decision, List<ViolationDto> violations, long totalTimeMs, boolean passed) {}
