package com.sporekart.ai.analytics.interfaces.rest.dto;

import java.util.UUID;

public record ExportRequestDto(
    UUID reportId,
    String format
) {}
