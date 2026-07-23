package com.sporekart.workspace.dto;

import com.sporekart.copilot.domain.ConversationTurn;
import java.util.List;

public record HistoryResponse(
    List<ConversationTurn> turns,
    int total,
    int page,
    int size
) {}
