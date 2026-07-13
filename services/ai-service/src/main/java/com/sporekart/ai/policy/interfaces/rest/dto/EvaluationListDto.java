package com.sporekart.ai.policy.interfaces.rest.dto;

import java.util.List;

public record EvaluationListDto(List<EvaluationResultDto> evaluations, int total) {}
