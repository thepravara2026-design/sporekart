package com.sporekart.marketplace.dto;

import java.util.List;

public record PluginListResponse(
    int total,
    int installed,
    int enabled,
    int errored,
    List<PluginResponse> plugins
) {}