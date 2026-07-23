package com.sporekart.customer.copilot.dto;

public record TrainingQueryRequest(
    String query,
    String category,
    String level,
    String location
) {
    public TrainingQueryRequest {
        if (query == null) {
            query = "";
        }
        if (category == null) {
            category = "";
        }
        if (level == null) {
            level = "";
        }
        if (location == null) {
            location = "";
        }
    }
}
