package com.sporekart.copilot.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreateSessionRequest(
    @NotBlank String copilotType,
    @NotBlank String userId,
    String userName,
    List<String> roles
) {}
