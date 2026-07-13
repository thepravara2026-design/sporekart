package com.sporekart.ai.decision.interfaces.rest.dto;
import java.util.List;
public record DecisionResponseDto(String id, String requestId, String action, String status, String confidence, String summary, List<ReasonDto> reasons, long processingTimeMs, boolean requiresApproval) {}
