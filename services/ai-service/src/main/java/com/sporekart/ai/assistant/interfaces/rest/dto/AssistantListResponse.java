package com.sporekart.ai.assistant.interfaces.rest.dto;

import java.util.List;

public record AssistantListResponse(
        List<AssistantResponse> assistants,
        int total
) {}
