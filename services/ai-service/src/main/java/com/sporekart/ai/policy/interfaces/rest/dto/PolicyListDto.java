package com.sporekart.ai.policy.interfaces.rest.dto;

import java.util.List;

public record PolicyListDto(List<PolicyResponseDto> policies, int total) {}
