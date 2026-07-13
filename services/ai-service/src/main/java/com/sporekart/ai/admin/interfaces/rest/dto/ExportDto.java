package com.sporekart.ai.admin.interfaces.rest.dto;

import java.util.Map;

public record ExportDto(
    String environment,
    Map<String, Object> configuration,
    String exportedAt
) {}
