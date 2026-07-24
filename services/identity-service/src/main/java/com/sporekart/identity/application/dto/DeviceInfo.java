package com.sporekart.identity.application.dto;

import jakarta.validation.constraints.NotBlank;

public record DeviceInfo(
        @NotBlank String deviceName,
        @NotBlank String deviceType,
        @NotBlank String os,
        @NotBlank String browser,
        @NotBlank String deviceIdentifier) {}
