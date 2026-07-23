package com.sporekart.identity.application.dto;

import jakarta.validation.constraints.NotBlank;

public record OtpVerifyRequest(
        @NotBlank String userId,
        @NotBlank String code,
        @NotBlank String purpose) {}
