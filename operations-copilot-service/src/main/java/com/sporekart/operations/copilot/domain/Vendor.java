package com.sporekart.operations.copilot.domain;

import java.util.List;

public record Vendor(
    String vendorId,
    String name,
    String category,
    List<String> productsSupplied,
    double reliabilityScore,
    double qualityScore,
    double deliveryScore,
    double overallScore,
    int averageLeadTimeDays,
    double pricingCompetitiveness,
    String region,
    boolean preferred
) {}
