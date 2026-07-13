package com.sporekart.ai.decision.interfaces.rest.dto;
import java.util.List;
public record HistoryDto(List<HistoryEntryDto> entries, int total) {}
