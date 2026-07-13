package com.sporekart.ai.automation.interfaces.rest.dto;

import java.util.UUID;

public record JobCancelDto(
    UUID jobId,
    String reason
) {}
