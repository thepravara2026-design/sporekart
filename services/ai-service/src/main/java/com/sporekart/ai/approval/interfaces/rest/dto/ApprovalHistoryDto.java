package com.sporekart.ai.approval.interfaces.rest.dto;

import java.util.List;

public record ApprovalHistoryDto(
    List<HistoryEntryDto> entries,
    int total
) {}
