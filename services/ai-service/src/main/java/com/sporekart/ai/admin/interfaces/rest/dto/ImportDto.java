package com.sporekart.ai.admin.interfaces.rest.dto;

import java.util.Map;

public record ImportDto(
    Map<String, Object> configuration,
    String environment,
    boolean dryRun
) {}
