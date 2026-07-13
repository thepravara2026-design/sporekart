package com.sporekart.ai.admin.interfaces.rest.dto;

import java.util.UUID;

public record RollbackDto(
    UUID configId,
    int targetVersion,
    String reason
) {}
