package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ERPSyncHistoryResponse {
    @Schema(description = "List of ERP synchronization history entries")
    private List<ERPSyncHistoryEntry> history;

    public ERPSyncHistoryResponse() {
    }

    public ERPSyncHistoryResponse(List<ERPSyncHistoryEntry> history) {
        this.history = history;
    }

    public List<ERPSyncHistoryEntry> getHistory() {
        return history;
    }

    public void setHistory(List<ERPSyncHistoryEntry> history) {
        this.history = history;
    }
}
