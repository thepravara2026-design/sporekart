package com.sporekart.operations.copilot.dto;

import java.util.List;

public record ProcurementResponse(
    String recommendation,
    double totalCost,
    double expectedSavings,
    List<VendorResponse> vendorSuggestions,
    List<String> actionItems
) {
    public record VendorResponse(String vendorId, String name, double totalScore, double unitPrice, int leadTimeDays, String riskLevel) {}
}
