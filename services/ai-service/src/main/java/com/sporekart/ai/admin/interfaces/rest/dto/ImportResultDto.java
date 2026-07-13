package com.sporekart.ai.admin.interfaces.rest.dto;

import java.util.List;

public record ImportResultDto(
    boolean success,
    int imported,
    int failed,
    List<String> errors,
    String message
) {}
