package com.sporekart.identity.application.dto;

import jakarta.validation.constraints.NotBlank;

public record WorkspaceRequest(@NotBlank String name) {}
