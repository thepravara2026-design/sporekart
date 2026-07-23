package com.sporekart.identity.application.dto;

import jakarta.validation.constraints.NotBlank;

public record OtpRequest(@NotBlank String userId, @NotBlank String purpose) {}
