package com.sporekart.ai.risk.interfaces.rest.dto;

import java.util.List;

public record TrustHistoryDto(
    List<TrustDto> entries,
    int total
) {}
