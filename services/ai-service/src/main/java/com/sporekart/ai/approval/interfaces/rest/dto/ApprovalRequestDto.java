package com.sporekart.ai.approval.interfaces.rest.dto;

import java.util.List;
import java.util.Map;

public record ApprovalRequestDto(
    String module,
    String action,
    Map<String, Object> payload,
    Map<String, Object> context,
    String userId,
    List<String> roles,
    String reason,
    String urgency
) {}
