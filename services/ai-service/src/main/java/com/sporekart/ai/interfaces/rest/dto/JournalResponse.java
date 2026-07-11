package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public class JournalResponse {
    @Schema(description = "Journal entry identifier", example = "d94a1f1f-6c9f-4ab8-84c1-115a7a6f5b7c")
    private UUID id;

    @Schema(description = "Human-readable journal number", example = "JE-20260710-001")
    private String journalNumber;

    @Schema(description = "Journal status", example = "DRAFT")
    private String status;

    public JournalResponse() {
    }

    public JournalResponse(UUID id, String journalNumber, String status) {
        this.id = id;
        this.journalNumber = journalNumber;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getJournalNumber() {
        return journalNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setJournalNumber(String journalNumber) {
        this.journalNumber = journalNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
