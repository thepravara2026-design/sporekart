package com.sporekart.ai.decision.interfaces.rest.dto;
import java.util.List;
public record ExplanationDto(String summary, List<String> matchedPolicies, List<String> triggeredRules, String confidence, String recommendedAction, String explanationText) {}
