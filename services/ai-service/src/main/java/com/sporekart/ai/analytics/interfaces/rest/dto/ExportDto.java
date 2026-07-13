package com.sporekart.ai.analytics.interfaces.rest.dto;

import java.util.UUID;

public record ExportDto(
    String id,
    UUID reportId,
    String format,
    String fileName,
    long fileSize,
    String exportedAt
) {}
