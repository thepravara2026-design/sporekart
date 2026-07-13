package com.sporekart.ai.assistant.interfaces.rest.dto;

import java.util.List;

public record HistoryResponse(
        List<ChatResponse> history,
        int total,
        int page,
        int size
) {}
