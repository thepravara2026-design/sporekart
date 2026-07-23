package com.sporekart.grower.copilot.dto;

import java.util.Map;

public record YieldRequest(
    String mushroomType,
    String substrateType,
    double areaSquareMeters,
    int numberOfBags,
    String farmLocation,
    Map<String, Object> parameters
) {}
