package com.sporekart.ai.decision.interfaces.rest.dto;
import java.util.Map;
public record StatisticsDto(long totalDecisions, long allowed, long denied, long escalated, long approvals, Map<String, Object> detailed) {}
