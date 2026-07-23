package com.sporekart.marketplace.dto;

import jakarta.validation.constraints.NotBlank;

public record PluginInstallRequest(
    @NotBlank String pluginId,
    String version,
    String source
) {}