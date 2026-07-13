package com.sporekart.ai.risk.interfaces.rest.dto;

import java.util.List;

public record ConfidenceHistoryDto(
    List<ConfidenceDto> entries,
    int total
) {}
