package com.sporekart.ai.risk.interfaces.rest.dto;

import java.util.List;

public record RiskHistoryDto(
    List<HistoryEntryDto> entries,
    int total
) {}
